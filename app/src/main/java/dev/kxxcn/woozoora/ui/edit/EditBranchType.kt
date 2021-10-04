package dev.kxxcn.woozoora.ui.edit

enum class EditBranchType {

    TRANSACTION,

    ASSET;

    companion object {

        fun find(ordinary: Int): EditBranchType {
            return values()[ordinary]
        }
    }
}
