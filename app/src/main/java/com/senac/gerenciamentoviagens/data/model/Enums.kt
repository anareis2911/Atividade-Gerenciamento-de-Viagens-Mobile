package com.senac.gerenciamentoviagens.data.model

enum class TaskStatus(val value: Int) {
    PENDING(0),
    DONE(1);

    companion object {
        fun fromInt(value: Int) = entries.first { it.value == value }
    }
}

enum class TaskPriority(val value: Int) {
    LOW(0),
    MEDIUM(1),
    HIGH(2);

    companion object {
        fun fromInt(value: Int) = entries.first { it.value == value }
    }
}
