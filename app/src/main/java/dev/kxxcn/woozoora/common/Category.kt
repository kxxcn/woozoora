package dev.kxxcn.woozoora.common

import dev.kxxcn.woozoora.R

enum class Category(
    val nameRes: Int,
    val colorRes: Int,
    val iconRes: Int,
) {

    FOOD_EXPENSES(
        R.string.category_food_expenses,
        R.color.categoryFoodExpenses,
        R.drawable.ic_emoji_hamburger
    ),
    TRANSPORTATION(
        R.string.category_transportation,
        R.color.categoryTransportation,
        R.drawable.ic_emoji_metro
    ),
    CULTURAL_LIFE(
        R.string.category_cultural_life,
        R.color.categoryCulturalLife,
        R.drawable.ic_emoji_sparkles
    ),
    MART(
        R.string.category_mart,
        R.color.categoryMart,
        R.drawable.ic_emoji_cart
    ),
    FASHION(
        R.string.category_fashion,
        R.color.categoryFashion,
        R.drawable.ic_emoji_lipstick
    ),
    HOUSEHOLD_GOODS(
        R.string.category_household_goods,
        R.color.categoryHouseholdGoods,
        R.drawable.ic_emoji_package
    ),
    TELECOMMUNICATION(
        R.string.category_telecommunication,
        R.color.categoryTelecommunication,
        R.drawable.ic_emoji_laptop
    ),
    HEALTH(
        R.string.category_health,
        R.color.categoryHealth,
        R.drawable.ic_emoji_healthy
    ),
    EDUCATION(
        R.string.category_education,
        R.color.categoryEducation,
        R.drawable.ic_emoji_books
    ),
    MEMBERSHIP_FEES(
        R.string.category_membership_fees,
        R.color.categoryMembershipFees,
        R.drawable.ic_emoji_ribbon
    ),
    PARENTS(
        R.string.category_parents,
        R.color.categoryParents,
        R.drawable.ic_emoji_hands
    ),
    ETC(
        R.string.category_etc,
        R.color.categoryEtc,
        R.drawable.ic_emoji_rainbow
    ),
    CAFE(
        R.string.category_cafe,
        R.color.categoryCafe,
        R.drawable.ic_emoji_cafe
    ),
    BABY(
        R.string.category_baby,
        R.color.categoryBaby,
        R.drawable.ic_emoji_baby
    ),
    MEDICAL(
        R.string.category_medical,
        R.color.categoryMedical,
        R.drawable.ic_emoji_medical
    ),
    LOAN(
        R.string.category_loan,
        R.color.categoryLoan,
        R.drawable.ic_emoji_loan
    ),
    INSURANCE(
        R.string.category_insurance,
        R.color.categoryInsurance,
        R.drawable.ic_emoji_insurance
    );

    companion object {

        fun find(ordinary: Int): Category {
            return values()[ordinary]
        }
    }
}
