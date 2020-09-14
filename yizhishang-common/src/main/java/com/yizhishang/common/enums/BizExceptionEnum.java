package com.yizhishang.common.enums;

/**
 * @author yizhishang
 */
public enum BizExceptionEnum implements CommonEnum {
    /**
     * 服务器异常
     */
    SERVER_ERROR(500, "server.error"),
    DATABASE_NOT_WORK(501, "database.not.work"),
    MANIPULATE_DATA_FAILED(501, "manipulate.data.failed"),
    MANIPULATE_CACHE_FAILED(501, "manipulate.cache.failed"),
    EXIST_UNKNOWN_ROLE(410, "exist.unknown.role"),
    DATABASE_HAS_TENANT(410, "database.has.tenant"),
    MENU_HAS_CHILD(410, "menu.has.child"),
    ROlE_IS_IN_USE(410, "role.is.in.use"),
    MENU_IS_IN_USE(410, "menu.is.in.use"),
    NO_DATA(411, "no.data"),
    CURRENT_USER_NOT_EXIST(411, "current.user.not.exist"),
    CURRENT_ROLE_NOT_EXIST(411, "current.role.not.exist"),
    CURRENT_MENU_NOT_EXIST(411, "current.menu.not.exist"),
    TENANT_NOT_FOUND(411, "tenant.not.found"),
    DATA_ALREADY_EXISTS(412, "data.already.exists"),
    TENANT_ALREADY_EXISTS(412, "tenant.already.exists"),
    REQUEST_DATA_ERROR(413, "request.data.error"),
    TENANT_CANT_BE_NULL(413, "tenant.can't.be.null"),
    HAS_NO_AUTHORITY_TO_CHECK(300, "has.no.authority.to.check"),
    HAS_NO_AUTHORITY_TO_OPERATE(301, "has.no.authority.to.operate"),
    CANT_DELETE_ADMIN(302, "can't.delete.admin"),
    CANT_FREEZE_ADMIN(302, "can't.freeze.admin"),
    CANT_CHANGE_ADMIN(302, "can't.change.admin"),
    ROLE_HAS_CHILD(410, "role.has.child"),
    EXIST_DICTIONARY_WITH_SAME_NAME(412, "exist.dictionary.with.same.name"),
    EXIST_DICTIONARY_WITH_SAME_CODE(412, "exist.dictionary.with.same.code"),
    EXIST_DICTIONARY_WITH_SAME_VALUE(412, "exist.dictionary.with.same.value"),
    DICTIONARY_HAS_CHILD(410, "dictionary.has.child"),
    ERROR_JSON(410, "error.json"),
    EXIST_USER_WITH_SAME_NAME_OR_ACCOUNT(412, "exist.user.with.same.name.or.account"),
    ORGANIZATION_HAS_CHILD(410, "organization.has.child"),
    ORGANIZATION_IS_IN_USE(410, "organization.is.in.use");

    private Integer code;
    private String message;

    private BizExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
