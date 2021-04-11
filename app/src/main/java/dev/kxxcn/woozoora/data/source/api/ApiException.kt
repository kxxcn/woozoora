package dev.kxxcn.woozoora.data.source.api

import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.base.BaseException

class InvalidRequestException : BaseException()

class InvalidResponseException : BaseException()

class NoResultException : BaseException(R.string.try_again_after_a_while)

class UserNotFoundException : BaseException(R.string.invalid_user_information)

class UserSaveException : BaseException(R.string.member_registration_failed)

class UserUpdateException : BaseException(R.string.try_again_after_a_while)

class TransactionSaveException : BaseException(R.string.account_registration_failed)

class TransactionDeleteException : BaseException(R.string.account_deletion_failed)

class UnverifiedUserException : BaseException(R.string.user_information_cannot_be_verified)

class SendAskException : BaseException(R.string.try_again_after_a_while)

class LeaveException : BaseException(R.string.try_again_after_a_while)
