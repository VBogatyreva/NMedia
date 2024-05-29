package ru.netology.nmedia
    fun numberFormat(number: Long): String {
        return when {
            number < 1_000 -> number.toString()
            number in 1_000..9_999 -> {
                val beforeDecimalPoint = number / 1_000
                val afterDecimalPart = (number % 1_000) / 100
                if (afterDecimalPart == 0.toLong()) {
                    String.format("%dK", beforeDecimalPoint)
                } else {
                    String.format("%d,%dK", beforeDecimalPoint, afterDecimalPart)
                }
            }

            number in 10_000..999_999 -> {
                val beforeDecimalPoint = number / 1_000
                String.format("%dK", beforeDecimalPoint)
            }

            number in 1_000_000..99_999_999 -> {
                val beforeDecimalPoint = number / 1_000_000
                val afterDecimalPart = (number % 1_000_000) / 100_000
                if (afterDecimalPart == 0.toLong()) {
                    String.format("%dМ", beforeDecimalPoint)
                } else {
                    String.format("%d,%dМ", beforeDecimalPoint, afterDecimalPart)
                }
            }

            else -> String.format("%.1fМ", number / 1_000_000.0)
        }
    }
