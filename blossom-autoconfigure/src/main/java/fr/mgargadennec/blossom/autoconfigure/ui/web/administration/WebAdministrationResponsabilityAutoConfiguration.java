package fr.mgargadennec.blossom.autoconfigure.ui.web.administration;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.autoconfigure.core.CommonAutoConfiguration;
import fr.mgargadennec.blossom.core.association_user_role.AssociationUserRoleService;
import fr.mgargadennec.blossom.core.role.RoleService;
import fr.mgargadennec.blossom.core.user.UserService;
import fr.mgargadennec.blossom.ui.web.administration.responsability.ResponsabilitiesController;

@Configuration
@ConditionalOnClass(ResponsabilitiesController.class)
@AutoConfigureAfter(CommonAutoConfiguration.class)
public class WebAdministrationResponsabilityAutoConfiguration {

  @Bean
  public ResponsabilitiesController responsabilityController(UserService userService,
      AssociationUserRoleService associationUserRoleService, RoleService roleService) {
    return new ResponsabilitiesController(userService, associationUserRoleService, roleService);
  }

}
