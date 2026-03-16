package ru.netoogy.nmedia.opportunities


    fun countToString(value: Int): String {
        return when {
            value > 999_999 -> "${value / 1_000_000}.${(value % 1_000_000) / 100_000}M"
            value in 10_000..999_999 -> "${value / 1_000}K"
            value > 999 -> "${value / 1_000}.${(value % 1_000) / 100}K"
            else -> "$value"
        }
    }
