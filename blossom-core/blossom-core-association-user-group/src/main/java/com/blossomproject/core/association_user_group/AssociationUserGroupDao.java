package com.blossomproject.core.association_user_group;

import com.blossomproject.core.common.dao.AssociationDao;
import com.blossomproject.core.group.Group;
import com.blossomproject.core.user.User;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public interface AssociationUserGroupDao extends AssociationDao<User, Group, AssociationUserGroup> {

}
