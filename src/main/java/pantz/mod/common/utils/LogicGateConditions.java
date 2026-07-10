package pantz.mod.common.utils;

@FunctionalInterface
public interface LogicGateConditions {
    /**
     * Logics for two and three inputs
     *
     * @param back  (optional) back input - not in two inputs
     * @param left  left input
     * @param right right input
     * @return boolean depends on logics
     */
    boolean apply(boolean back, boolean left, boolean right);

    default boolean apply(boolean left, boolean right) {
        return apply(false, left, right);
    }

    default LogicGateConditions not() {
        return (b, l, r) -> !this.apply(b, l, r);
    }

    // ===== 2-input =====
    LogicGateConditions AND = (b, l, r) -> l && r;
    LogicGateConditions OR = (b, l, r) -> l || r;
    LogicGateConditions XOR = (b, l, r) -> l ^ r;

    // ===== NOT 2-input =====
    LogicGateConditions NAND = AND.not();
    LogicGateConditions NOR = OR.not();
    LogicGateConditions XNOR = XOR.not();

    // ===== 3-input =====
    LogicGateConditions ADVANCED_AND = (b, l, r) -> b && l && r;
    LogicGateConditions ADVANCED_OR = (b, l, r) -> b || l || r;
    LogicGateConditions ADVANCED_XOR = (b, l, r) -> b ^ l ^ r;

    // ===== NOT 3-input =====
    LogicGateConditions ADVANCED_NAND = ADVANCED_AND.not();
    LogicGateConditions ADVANCED_NOR = ADVANCED_OR.not();
    LogicGateConditions ADVANCED_XNOR = ADVANCED_XOR.not();

    // ===== Multi-input =====
    LogicGateConditions MAJORITY = (b, l, r) -> (b ? 1 : 0) + (l ? 1 : 0) + (r ? 1 : 0) >= 2;
    LogicGateConditions MINORITY = MAJORITY.not();
}
