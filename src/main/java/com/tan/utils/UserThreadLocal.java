package com.tan.utils;

import com.tan.entity.EntityUser;

/**
 * 利用ThreadLocal存储登录后的用户信息
 */
public class UserThreadLocal {
   private static final ThreadLocal<EntityUser> LOCAL = new ThreadLocal<>();

   private UserThreadLocal() {}

   public static void put(EntityUser user) {
      LOCAL.set(user);
   }
   public static EntityUser get() {
      return LOCAL.get();
   }
   public static void remove() {
      LOCAL.remove();
   }
}
