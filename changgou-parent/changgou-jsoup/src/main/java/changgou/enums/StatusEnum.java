package dh.framework.common.constant.db;

import java.util.Arrays;
import lombok.Getter;

/**
 * description
 *
 * @author yong
 * @date 2019/1/27 14:46
 */
@Getter
public enum StatusEnum {

    OK(0),

    DISABLE(1),

    DELETE(2);

    private int status;

    StatusEnum(int status) {
        this.status = status;
    }

    public static final Iterable<Integer> serachable() {
        return Arrays.asList(StatusEnum.OK.status(), StatusEnum.DISABLE.status());
    }

    public static final Iterable<Integer> queryable() {
        return Arrays.asList(StatusEnum.OK.status(), StatusEnum.DISABLE.status());
    }

    public static final int ok() {
        return StatusEnum.OK.status;
    }

    public static final boolean isOk(int v) {
        return StatusEnum.OK.status == v;
    }

    public static final int disable() {
        return StatusEnum.DISABLE.status;
    }

    public static final boolean isDisable(int v) {
        return StatusEnum.DISABLE.status == v;
    }

    public static final int delete() {
        return StatusEnum.DELETE.status;
    }

    public static final boolean isDelete(int v) {
        return StatusEnum.DELETE.status == v;
    }

    public int status() {
        return this.status;
    }
}
