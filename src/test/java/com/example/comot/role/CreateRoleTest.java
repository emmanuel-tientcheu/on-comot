package com.example.comot.role;

import com.example.comot.role.application.ports.RoleRepository;
import com.example.comot.role.application.useCases.CreateRoleCommand;
import com.example.comot.role.application.useCases.CreateRoleCommandHandler;
import com.example.comot.role.infrastructure.persistance.ram.InMemoryRoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CreateRoleTest {

    @Test
    void shouldCreateRole() {
        RoleRepository repository = new InMemoryRoleRepository();

        var command = new CreateRoleCommand(
                "title",
                "description",
                true
        );

        var useCase = new CreateRoleCommandHandler(repository);

        var idResponse = useCase.handle(command);

        var roleQuery = repository.findById(idResponse.getId());
        Assertions.assertTrue(roleQuery.isPresent());

        var role = roleQuery.get();
        Assertions.assertEquals(role.getTitle(), command.getTitle());

    }
}
