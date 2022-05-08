package cn.chf.lightjob.enums;

/**
 * Borrowed from xxl-job v2.1.0
 */
public enum MisfireStrategyEnum {

    /**
     * do nothing
     */
    DO_NOTHING("忽略"),

    /**
     * fire once now
     */
    FIRE_ONCE_NOW("立即执行一次");

    private String title;

    MisfireStrategyEnum(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static MisfireStrategyEnum match(String name, MisfireStrategyEnum defaultItem){
        for (MisfireStrategyEnum item: MisfireStrategyEnum.values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return defaultItem;
    }
}
