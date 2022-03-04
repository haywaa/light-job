package com.chf.lightjob.enums;

/**
 * Borrowed from xxl-job v2.1.0
 */
public enum BlockStrategyEnum {

    SERIAL_EXECUTION("串行执行"),
    CONCURRENT_EXECUTION("并发执行"),
    DISCARD_LATER("忽略新任务"),
    //COVER_EARLY("Cover Early"),
    ;

    private String title;
    BlockStrategyEnum(String title) {
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public static BlockStrategyEnum match(String name, BlockStrategyEnum defaultItem) {
        if (name != null) {
            for (BlockStrategyEnum item: BlockStrategyEnum.values()) {
                if (item.name().equals(name)) {
                    return item;
                }
            }
        }
        return defaultItem;
    }
}
