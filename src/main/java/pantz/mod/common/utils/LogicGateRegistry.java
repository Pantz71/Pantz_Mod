package pantz.mod.common.utils;

import net.minecraft.util.StringRepresentable;

public enum LogicGateRegistry implements StringRepresentable {
    // 2-input
    AND("and", LogicGateConditions.AND),
    OR("or", LogicGateConditions.OR),
    NAND("nand", LogicGateConditions.NAND),
    NOR("nor", LogicGateConditions.NOR),
    XOR("xor", LogicGateConditions.XOR),
    XNOR("xnor", LogicGateConditions.XNOR),

    // 3-input
    ADVANCED_AND("advanced_and", LogicGateConditions.ADVANCED_AND),
    ADVANCED_OR("advanced_or", LogicGateConditions.ADVANCED_OR),
    ADVANCED_NAND("advanced_nand", LogicGateConditions.ADVANCED_NAND),
    ADVANCED_NOR("advanced_nor", LogicGateConditions.ADVANCED_NOR),
    ADVANCED_XOR("advanced_xor", LogicGateConditions.ADVANCED_XOR),
    ADVANCED_XNOR("advanced_xnor", LogicGateConditions.ADVANCED_XNOR),

    // Multi-input
    MAJORITY("majority", LogicGateConditions.MAJORITY),
    MINORITY("minority", LogicGateConditions.MINORITY);

    @SuppressWarnings("deprecation")
    public static final EnumCodec<LogicGateRegistry> CODEC = StringRepresentable.fromEnum(LogicGateRegistry::values);

    private final String name;
    private final LogicGateConditions logic;

    LogicGateRegistry(String name, LogicGateConditions logic) {
        this.name = name;
        this.logic = logic;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public boolean apply(boolean left, boolean right) {
        return this.logic.apply(left, right);
    }

    public boolean apply(boolean back, boolean left, boolean right) {
        return this.logic.apply(back, left, right);
    }

}
