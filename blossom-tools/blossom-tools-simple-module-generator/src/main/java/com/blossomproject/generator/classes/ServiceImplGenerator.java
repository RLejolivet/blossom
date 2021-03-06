package com.blossomproject.generator.classes;

import com.helger.jcodemodel.AbstractJClass;
import com.helger.jcodemodel.JBlock;
import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;
import com.helger.jcodemodel.JExpr;
import com.helger.jcodemodel.JMethod;
import com.helger.jcodemodel.JMod;
import com.helger.jcodemodel.JVar;
import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.core.common.event.CreatedEvent;
import com.blossomproject.core.common.event.UpdatedEvent;
import com.blossomproject.core.common.service.AssociationServicePlugin;
import com.blossomproject.core.common.service.GenericCrudServiceImpl;
import com.blossomproject.generator.configuration.model.Field;
import com.blossomproject.generator.configuration.model.Settings;
import com.blossomproject.generator.utils.GeneratorUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.plugin.core.PluginRegistry;

public class ServiceImplGenerator implements ClassGenerator {

  private AbstractJClass entityClass;
  private AbstractJClass daoClass;
  private AbstractJClass mapperClass;
  private AbstractJClass dtoClass;
  private AbstractJClass serviceClass;
  private AbstractJClass createFormClass;
  private AbstractJClass updateFormClass;

  @Override
  public void prepare(Settings settings, JCodeModel codeModel) {
    this.entityClass = codeModel.ref(GeneratorUtils.getEntityFullyQualifiedClassName(settings));
    this.daoClass = codeModel.ref(GeneratorUtils.getDaoFullyQualifiedClassName(settings));
    this.mapperClass = codeModel.ref(GeneratorUtils.getMapperFullyQualifiedClassName(settings));
    this.dtoClass = codeModel.ref(GeneratorUtils.getDtoFullyQualifiedClassName(settings));
    this.serviceClass = codeModel.ref(GeneratorUtils.getServiceFullyQualifiedClassName(settings));
    this.createFormClass = codeModel
      .ref(GeneratorUtils.getCreateFormFullyQualifiedClassName(settings));
    this.updateFormClass = codeModel
      .ref(GeneratorUtils.getUpdateFormFullyQualifiedClassName(settings));
  }

  @Override
  public JDefinedClass generate(Settings settings, JCodeModel codeModel) {
    try {

      JDefinedClass definedClass = codeModel
        ._class(GeneratorUtils.getServiceImplFullyQualifiedClassName(settings));
      definedClass
        ._extends(codeModel.ref(GenericCrudServiceImpl.class).narrow(dtoClass, entityClass));
      definedClass._implements(serviceClass);

      JMethod constructor = definedClass.constructor(JMod.PUBLIC);
      constructor.body().invoke("super")
        .arg(constructor.param(daoClass, "dao"))
        .arg(constructor.param(mapperClass, "mapper"))
        .arg(constructor.param(ApplicationEventPublisher.class, "publisher"))
        .arg(constructor.param(codeModel.ref(PluginRegistry.class)
            .narrow(codeModel.ref(AssociationServicePlugin.class),
              codeModel.ref(Class.class).narrow(codeModel.ref(AbstractDTO.class).wildcardExtends())),
          "associationRegistry"));

      buildCreate(settings, definedClass, codeModel);
      buildUpdate(settings, definedClass, codeModel);

      return definedClass;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Can't generate repository class", e);
    }
  }

  private void buildCreate(Settings settings, JDefinedClass definedClass, JCodeModel codeModel) {
    JMethod create = definedClass.method(JMod.PUBLIC, dtoClass, "create");
    create.annotate(Override.class);
    JVar form = create.param(createFormClass, "createForm");

    JBlock body = create.body();
    JVar toCreate = body.decl(dtoClass, "toCreate", JExpr._new(dtoClass));
    for (Field field : settings.getFields()) {
      if (field.isRequiredCreate()) {
        body.add(toCreate.invoke(field.getSetterName()).arg(form.invoke(field.getGetterName())));
      }
    }
    body._return(JExpr._this().invoke("create").arg(toCreate));
  }

  private void buildUpdate(Settings settings, JDefinedClass definedClass, JCodeModel codeModel) {
    JMethod update = definedClass.method(JMod.PUBLIC, dtoClass, "update");
    update.annotate(Override.class);
    JVar id = update.param(Long.class, "id");
    JVar form = update.param(updateFormClass, "updateForm");

    JBlock body = update.body();
    JVar toUpdate = body.decl(dtoClass, "toUpdate", JExpr._this().invoke("getOne").arg(id));
    for (Field field : settings.getFields()) {
      if (field.isPossibleUpdate()) {
        body.add(toUpdate.invoke(field.getSetterName()).arg(form.invoke(field.getGetterName())));
      }
    }

    body._return(JExpr._this().invoke("update").arg(id).arg(toUpdate));

  }


}
