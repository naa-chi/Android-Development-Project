#!/usr/bin/env python3
"""
generate_tests.py — auto-generates JUnit 6 / Mockito 5 unit tests for
every Service and Controller in the Juntas Vecinos microservices.

Spring Boot 4.1.1 / Spring Framework 7 / JUnit 6 compatible.

Usage:
    python generate_tests.py --root ./Server
    python generate_tests.py --root ./Server --dry-run
"""

from __future__ import annotations
import argparse
import sys
from pathlib import Path
from dataclasses import dataclass, field

# ---------------------------------------------------------------------------
# Service registry
# ---------------------------------------------------------------------------
@dataclass
class ServiceConfig:
    directory: str
    package: str
    service_class: str
    controller_class: str
    service_methods: list[str]
    controller_path: str
    controller_methods: list[str]
    mapper: str = ""
    extra_mocks: list[tuple[str, str]] = field(default_factory=list)
    extra_imports: list[str] = field(default_factory=list)
    create_stubs: list[str] = field(default_factory=list)
    update_stubs: list[str] = field(default_factory=list)
    valid_create_json: str = "{}"
    custom_service_template: bool = False
    custom_controller_template: bool = False


REGISTRY: list[ServiceConfig] = [
    ServiceConfig(
        directory="event",
        package="com.juntas.event",
        service_class="EventService",
        controller_class="EventController",
        service_methods=["create", "findById", "findAll", "findByNeighbor", "update", "delete"],
        controller_path="/api/v1/events",
        controller_methods=["GET", "GET_ALL", "POST", "PUT", "DELETE"],
        mapper="EventMapper",
        extra_mocks=[
            ("com.juntas.event.client.VenueClient", "venueClient"),
            ("com.juntas.event.security.CurrentUserProvider", "currentUser"),
        ],
        extra_imports=["com.juntas.event.client.dto.VenueDto"],
        create_stubs=[
            "when(currentUser.getUserId()).thenReturn(1L);",
            'when(venueClient.getById(any())).thenReturn(new VenueDto(1L, "test"));',
        ],
        update_stubs=[
            "when(currentUser.getUserId()).thenReturn(1L);",
            'when(venueClient.getById(any())).thenReturn(new VenueDto(1L, "test"));',
        ],
        valid_create_json=(
            '{"title":"Valid Title","description":"Valid description",'
            '"eventType":"PARTY","venueId":1,"date":"2030-01-01",'
            '"startTime":"10:00:00","endTime":"12:00:00"}'
        ),
    ),
    ServiceConfig(
        directory="expenses",
        package="com.juntas.expenses",
        service_class="EventExpenseService",
        controller_class="EventExpenseController",
        service_methods=["create", "findById", "findAll", "findByNeighborhood", "findByUser", "update", "delete"],
        controller_path="/api/v1/event-expenses",
        controller_methods=["GET", "GET_ALL", "POST", "PUT", "DELETE"],
        mapper="EventExpenseMapper",
        valid_create_json=(
            '{"emissionDate":"2020-01-01","bill":100,"forUserId":1,'
            '"forEmail":"a@b.com","eventId":1,"neighborhoodId":1}'
        ),
    ),
    ServiceConfig(
        directory="expenses",
        package="com.juntas.expenses",
        service_class="UtilityExpenseService",
        controller_class="UtilityExpenseController",
        service_methods=["create", "findById", "findAll", "findByNeighborhood", "findByUser", "update", "delete"],
        controller_path="/api/v1/utilityexpense",
        controller_methods=["GET", "GET_ALL", "POST", "PUT", "DELETE"],
        mapper="UtilityExpenseMapper",
        valid_create_json=(
            '{"emissionDate":"2020-01-01","bill":100,"utilType":"GAS",'
            '"forUserId":1,"forEmail":"a@b.com","neighborhoodId":1}'
        ),
    ),
    ServiceConfig(
        directory="neighborhood",
        package="com.juntas.neighborhood",
        service_class="NeighborhoodService",
        controller_class="NeighborhoodController",
        service_methods=["create", "findById", "findAll", "update", "delete"],
        controller_path="/api/v1/neighborhood",
        controller_methods=["GET", "GET_ALL", "POST", "PUT", "DELETE"],
        mapper="NeighborhoodMapper",
        valid_create_json='{"neighborhoodName":"Test Hood","populationNumber":1000}',
    ),
    ServiceConfig(
        directory="venue",
        package="com.juntas.venue",
        service_class="VenueService",
        controller_class="VenueController",
        service_methods=["create", "findById", "findAll", "update", "delete"],
        controller_path="/api/v1/venues",
        controller_methods=["GET", "GET_ALL", "POST", "PUT", "DELETE"],
        mapper="VenueMapper",
        extra_mocks=[("com.juntas.venue.client.NeighborhoodClient", "neighborhoodClient")],
        extra_imports=["com.juntas.venue.client.dto.NeighborhoodDto"],
        create_stubs=[
            'when(neighborhoodClient.getById(any())).thenReturn(new NeighborhoodDto(1L, "test"));',
        ],
        update_stubs=[
            'when(neighborhoodClient.getById(any())).thenReturn(new NeighborhoodDto(1L, "test"));',
        ],
        valid_create_json=(
            '{"name":"Test Venue","type":"HALL","pricePerHour":100,'
            '"capacity":50,"neighbourhoodId":1,"venueSizeMSquared":100}'
        ),
    ),
    # ---- AuthService (custom template) -----------------------------------
    ServiceConfig(
        directory="users",
        package="com.juntas.users",
        service_class="AuthService",
        controller_class="AuthController",
        service_methods=["register", "login"],
        controller_path="/api/v1/auth",
        controller_methods=["POST"],
        mapper="UserMapper",
        extra_mocks=[
            ("org.springframework.security.crypto.password.PasswordEncoder", "passwordEncoder"),
            ("com.juntas.users.security.JwtService", "jwtService"),
        ],
        valid_create_json=(
            '{"rut":"12345678","name":"Test User","dv":"9",'
            '"email":"test@example.com","password":"password123","neighborhoodId":1}'
        ),
        custom_service_template=True,
        custom_controller_template=True,
    ),
    # ---- UserService (custom template) ----------------------------------
    ServiceConfig(
        directory="users",
        package="com.juntas.users",
        service_class="UserService",
        controller_class="UserController",
        service_methods=["me", "findById", "findAll"],
        controller_path="/api/v1/users",
        controller_methods=["GET", "GET_ALL"],
        mapper="UserMapper",
        extra_mocks=[("com.juntas.users.security.CurrentUserProvider", "currentUser")],
        custom_service_template=True,
        custom_controller_template=True,
    ),
]

# ---------------------------------------------------------------------------
# Templates
# ---------------------------------------------------------------------------

SERVICE_TEST_TEMPLATE = '''\
package {package}.service;

import {package}.dto.*;
import {package}.exception.*;
import {package}.model.*;
import {package}.repository.*;
{extra_imports}
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class {service}Test {{

    @Mock private {repository} repository;
    @Mock private {mapper} mapper;
{extra_mock_fields}

    @InjectMocks
    private {service} service;

    @Test
    void findById_returnsEntity() {{
        var entity = new {entity}();
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(any())).thenReturn(mock({response}.class));

        var result = service.findById(1L);

        assertNotNull(result);
        verify(repository).findById(1L);
    }}

    @Test
    void findById_throwsWhenMissing() {{
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows({notFoundException}.class, () -> service.findById(99L));
    }}

    @Test
    void findAll_returnsList() {{
        when(repository.findAll()).thenReturn(List.of(new {entity}()));
        when(mapper.toResponse(any())).thenReturn(mock({response}.class));

        var result = service.findAll();

        assertEquals(1, result.size());
    }}

    @Test
    void create_persistsEntity() {{
        var request = mock({createRequest}.class);
        when(mapper.toEntity(any())).thenReturn(new {entity}());
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toResponse(any())).thenReturn(mock({response}.class));
{create_stubs}
        var result = service.create(request);

        assertNotNull(result);
        verify(repository).save(any());
    }}

    @Test
    void update_modifiesExisting() {{
        var existing = new {entity}();
        var request = mock({updateRequest}.class);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toResponse(any())).thenReturn(mock({response}.class));
        doNothing().when(mapper).applyUpdate(any(), any());
{update_stubs}
        var result = service.update(1L, request);

        assertNotNull(result);
        verify(repository).save(any());
    }}

    @Test
    void delete_removesExisting() {{
        when(repository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }}

    @Test
    void delete_throwsWhenMissing() {{
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows({notFoundException}.class, () -> service.delete(99L));
    }}
}}
'''

CONTROLLER_TEST_TEMPLATE = '''\
package {package}.controller;

import {package}.dto.*;
import {package}.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(controllers = {controller}.class)
@AutoConfigureMockMvc(addFilters = false)
class {controller}Test {{

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private {service} service;

    @Test
    void getById_returns200() throws Exception {{
        when(service.findById(1L)).thenReturn(mock({response}.class));

        mockMvc.perform(MockMvcRequestBuilders.get("{path}/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }}

    @Test
    void getAll_returns200() throws Exception {{
        when(service.findAll()).thenReturn(List.of(mock({response}.class)));

        mockMvc.perform(MockMvcRequestBuilders.get("{path}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }}

    @Test
    void create_returns201() throws Exception {{
        when(service.create(any())).thenReturn(mock({response}.class));

        mockMvc.perform(MockMvcRequestBuilders.post("{path}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{valid_create_json}"))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }}

    @Test
    void update_returns200() throws Exception {{
        when(service.update(eq(1L), any())).thenReturn(mock({response}.class));

        mockMvc.perform(MockMvcRequestBuilders.put("{path}/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{{}}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }}

    @Test
    void delete_returns204() throws Exception {{
        doNothing().when(service).delete(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("{path}/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }}
}}
'''

AUTH_SERVICE_TEST_TEMPLATE = '''\
package {package}.service;

import {package}.dto.*;
import {package}.exception.*;
import {package}.model.*;
import {package}.repository.*;
import {package}.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class {service}Test {{

    @Mock private UserRepository repository;
    @Mock private UserMapper mapper;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;

    @InjectMocks
    private {service} service;

    @Test
    void register_persistsUser() {{
        var request = mock(RegisterRequest.class);
        when(repository.existsByEmail(any())).thenReturn(false);
        when(repository.existsByRutNumeric(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("hash");
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toResponse(any())).thenReturn(mock(UserResponse.class));
        when(jwtService.generate(any())).thenReturn("token");

        var result = service.register(request);

        assertNotNull(result);
        verify(repository).save(any());
    }}

    @Test
    void login_returnsToken() {{
        var request = mock(LoginRequest.class);
        var user = new UserModel();
        when(repository.findByEmail(any())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtService.generate(any())).thenReturn("token");
        when(mapper.toResponse(any())).thenReturn(mock(UserResponse.class));

        var result = service.login(request);

        assertNotNull(result);
        verify(jwtService).generate(user);
    }}
}}
'''

AUTH_CONTROLLER_TEST_TEMPLATE = '''\
package {package}.controller;

import {package}.dto.*;
import {package}.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {{

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService service;

    @Test
    void register_returns201() throws Exception {{
        when(service.register(any())).thenReturn(mock(AuthResponse.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{valid_create_json}"))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }}

    @Test
    void login_returns200() throws Exception {{
        when(service.login(any())).thenReturn(mock(AuthResponse.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{{\\"email\\":\\"test@example.com\\",\\"password\\":\\"password123\\"}}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }}
}}
'''

USER_SERVICE_TEST_TEMPLATE = '''\
package {package}.service;

import {package}.dto.*;
import {package}.exception.*;
import {package}.model.*;
import {package}.repository.*;
import {package}.security.CurrentUserProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class {service}Test {{

    @Mock private UserRepository repository;
    @Mock private UserMapper mapper;
    @Mock private CurrentUserProvider currentUser;

    @InjectMocks
    private {service} service;

    @Test
    void me_returnsUser() {{
        var entity = new UserModel();
        when(currentUser.getUserId()).thenReturn(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(any())).thenReturn(mock(UserResponse.class));

        var result = service.me();

        assertNotNull(result);
    }}

    @Test
    void findById_returnsUser() {{
        var entity = new UserModel();
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(any())).thenReturn(mock(UserResponse.class));

        var result = service.findById(1L);

        assertNotNull(result);
    }}

    @Test
    void findAll_returnsList() {{
        when(repository.findAll()).thenReturn(List.of(new UserModel()));
        when(mapper.toResponse(any())).thenReturn(mock(UserResponse.class));

        var result = service.findAll();

        assertEquals(1, result.size());
    }}
}}
'''

USER_CONTROLLER_TEST_TEMPLATE = '''\
package {package}.controller;

import {package}.dto.*;
import {package}.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {{

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService service;

    @Test
    void me_returns200() throws Exception {{
        when(service.me()).thenReturn(mock(UserResponse.class));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/me"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }}

    @Test
    void getById_returns200() throws Exception {{
        when(service.findById(1L)).thenReturn(mock(UserResponse.class));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }}

    @Test
    void getAll_returns200() throws Exception {{
        when(service.findAll()).thenReturn(List.of(mock(UserResponse.class)));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }}
}}
'''

# ---------------------------------------------------------------------------
# Generator
# ---------------------------------------------------------------------------

def render_extra_mock_fields(cfg: ServiceConfig) -> str:
    if not cfg.extra_mocks:
        return ""
    lines = []
    for type_name, field_name in cfg.extra_mocks:
        simple = type_name.rsplit(".", 1)[-1]
        lines.append(f"    @Mock private {simple} {field_name};")
    return "\n".join(lines)


def render_stubs(stubs: list[str]) -> str:
    if not stubs:
        return ""
    return "\n".join("        " + s for s in stubs)


def render_extra_imports(imports: list[str]) -> str:
    if not imports:
        return ""
    return "\n".join(f"import {i};" for i in imports)


def render_service_test(cfg: ServiceConfig) -> str:
    if cfg.service_class == "AuthService":
        return AUTH_SERVICE_TEST_TEMPLATE.format(
            package=cfg.package,
            service=cfg.service_class,
        )
    if cfg.service_class == "UserService":
        return USER_SERVICE_TEST_TEMPLATE.format(
            package=cfg.package,
            service=cfg.service_class,
        )

    m = {
        "EventService": dict(
            repository="EventRepository",
            entity="EventModel",
            createRequest="CreateEventRequest",
            updateRequest="UpdateEventRequest",
            response="EventResponse",
            notFoundException="EventNotFoundException",
        ),
        "EventExpenseService": dict(
            repository="EventExpenseRepository",
            entity="EventExpenseModel",
            createRequest="CreateEventExpenseRequest",
            updateRequest="UpdateEventExpenseRequest",
            response="EventExpenseResponse",
            notFoundException="EventExpenseNotFoundException",
        ),
        "UtilityExpenseService": dict(
            repository="UtilityExpenseRepository",
            entity="UtilityExpenseModel",
            createRequest="CreateUtilityExpenseRequest",
            updateRequest="UpdateUtilityExpenseRequest",
            response="UtilityExpenseResponse",
            notFoundException="UtilityExpenseNotFoundException",
        ),
        "NeighborhoodService": dict(
            repository="NeighborhoodRepository",
            entity="NeighborhoodModel",
            createRequest="CreateNeighborhoodRequest",
            updateRequest="UpdateNeighborhoodRequest",
            response="NeighborhoodResponse",
            notFoundException="NeighborhoodNotFoundException",
        ),
        "VenueService": dict(
            repository="VenueRepository",
            entity="VenueModel",
            createRequest="CreateVenueRequest",
            updateRequest="UpdateVenueRequest",
            response="VenueResponse",
            notFoundException="VenueNotFoundException",
        ),
    }[cfg.service_class]

    return SERVICE_TEST_TEMPLATE.format(
        package=cfg.package,
        service=cfg.service_class,
        repository=m["repository"],
        entity=m["entity"],
        createRequest=m["createRequest"],
        updateRequest=m["updateRequest"],
        response=m["response"],
        notFoundException=m["notFoundException"],
        mapper=cfg.mapper,
        extra_mock_fields=render_extra_mock_fields(cfg),
        extra_imports=render_extra_imports(cfg.extra_imports),
        create_stubs=render_stubs(cfg.create_stubs),
        update_stubs=render_stubs(cfg.update_stubs),
    )


def render_controller_test(cfg: ServiceConfig) -> str:
    if cfg.service_class == "AuthService":
        return AUTH_CONTROLLER_TEST_TEMPLATE.format(
            package=cfg.package,
            valid_create_json=cfg.valid_create_json,
        )
    if cfg.service_class == "UserService":
        return USER_CONTROLLER_TEST_TEMPLATE.format(package=cfg.package)

    m = {
        "EventService": dict(response="EventResponse"),
        "EventExpenseService": dict(response="EventExpenseResponse"),
        "UtilityExpenseService": dict(response="UtilityExpenseResponse"),
        "NeighborhoodService": dict(response="NeighborhoodResponse"),
        "VenueService": dict(response="VenueResponse"),
    }[cfg.service_class]

    return CONTROLLER_TEST_TEMPLATE.format(
        package=cfg.package,
        controller=cfg.controller_class,
        service=cfg.service_class,
        response=m["response"],
        path=cfg.controller_path,
        valid_create_json=cfg.valid_create_json.replace("{", "{{").replace("}", "}}"),
    )


def write_test(root: Path, cfg: ServiceConfig, kind: str, content: str, dry: bool) -> Path:
    test_dir = root / cfg.directory / "src" / "test" / "java" / cfg.package.replace(".", "/") / kind
    test_dir.mkdir(parents=True, exist_ok=True)
    filename = f"{cfg.service_class if kind == 'service' else cfg.controller_class}Test.java"
    path = test_dir / filename

    if dry:
        print(f"[DRY-RUN] would write {path}")
    else:
        path.write_text(content, encoding="utf-8")
        print(f"✓ wrote {path}")

    return path


def main() -> int:
    ap = argparse.ArgumentParser(description="Generate JUnit 6 tests for microservices")
    ap.add_argument("--root", required=True, help="Path to the Server/ directory")
    ap.add_argument("--dry-run", action="store_true", help="Don't write files")
    args = ap.parse_args()

    root = Path(args.root).resolve()
    if not root.is_dir():
        print(f"error: {root} is not a directory", file=sys.stderr)
        return 2

    for cfg in REGISTRY:
        service_test = render_service_test(cfg)
        write_test(root, cfg, "service", service_test, args.dry_run)

        if cfg.controller_class:
            controller_test = render_controller_test(cfg)
            write_test(root, cfg, "controller", controller_test, args.dry_run)

    print("\nDone.")
    return 0


if __name__ == "__main__":
    sys.exit(main())