package fr.nodesigner.meaoo.mqtt.android.constant;

/**
 * QOS policy enum
 *
 * @author Bertrand Martel
 *
 */
enum class QosPolicy(val value: Int) {
    QOS_POLICY_AT_MOST_ONCE(0),
    QOS_POLICY_AT_LEAST_ONCE(1),
    QOS_POLICY_ONCE(2)
}