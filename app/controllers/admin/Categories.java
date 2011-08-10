package controllers.admin;

import models.Category;
import play.mvc.With;
import controllers.CRUD;
import controllers.Secure;

@CRUD.For(Category.class)
@With(Secure.class)
public class Categories extends CRUD {

}
