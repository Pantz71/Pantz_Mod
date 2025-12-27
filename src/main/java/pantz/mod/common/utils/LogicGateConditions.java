package pantz.mod.common.utils;

@FunctionalInterface
public interface LogicGateConditions {
    boolean apply(boolean back, boolean left, boolean right);

    default boolean apply(boolean left, boolean right) {
        return apply(false, left, right);
    }

    default LogicGateConditions not() {
        return (b, l, r) -> !this.apply(b, l, r);
    }

    // ===== Basic 2-input Gates =====
    LogicGateConditions AND = (b, l, r) -> l && r;
    LogicGateConditions OR = (b, l, r) -> l || r;
    LogicGateConditions XOR = (b, l, r) -> l ^ r;

    // ===== Inverted 2-input Gates =====
    LogicGateConditions NAND = AND.not();
    LogicGateConditions NOR = OR.not();
    LogicGateConditions XNOR = XOR.not();

    // ===== 3-input Gates =====
    LogicGateConditions ADVANCED_AND = (b, l, r) -> b && l && r;
    LogicGateConditions ADVANCED_OR = (b, l, r) -> b || l || r;
    LogicGateConditions ADVANCED_XOR = (b, l, r) -> b ^ l ^ r;

    // ===== Inverted 3-input Gates =====
    LogicGateConditions ADVANCED_NAND = ADVANCED_AND.not();
    LogicGateConditions ADVANCED_NOR = ADVANCED_OR.not();
    LogicGateConditions ADVANCED_XNOR = ADVANCED_XOR.not();

    // ===== Multi-input Logic =====
    LogicGateConditions MAJORITY = (b, l, r) -> (b ? 1 : 0) + (l ? 1 : 0) + (r ? 1 : 0) >= 2;
    LogicGateConditions MINORITY = MAJORITY.not();
}
