package com.sharingmap.security.confirmationtoken

import com.sharingmap.user.UserEntity
import com.sharingmap.user.UserRepository
import com.sharingmap.security.email.EmailService
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class ConfirmationTokenServiceImpl(private val confirmationTokenRepository: ConfirmationTokenRepository,
                                   private val emailService: EmailService,
                                   private val userRepository: UserRepository
)
    : ConfirmationTokenService {

    override fun saveConfirmationToken(token: ConfirmationTokenEntity) {
        confirmationTokenRepository.save(token)
    }

    override fun resendToken(email: String): ConfirmationTokenEntity? {
        val user: UserEntity? = userRepository.findByEmail(email)
        user?.let { confirmationTokenRepository.deleteByUser(it) }
        val confirmationToken = user?.let { createConfirmationToken(it) }

        return confirmationToken
    }

    override fun createConfirmationToken(user: UserEntity): ConfirmationTokenEntity {
        val token = getRandomString()

        val confirmationToken =  ConfirmationTokenEntity(token, LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(15), user)

        saveConfirmationToken(confirmationToken)

        buildEmail(user.username, token).let { emailService.sendConfirmationLetter(user.email, it) }
        return confirmationToken
    }

    override fun getToken(tokenId: UUID): Optional<ConfirmationTokenEntity> {
        return confirmationTokenRepository.findById(tokenId)
    }

    override fun deleteToken(id: UUID): String {
        confirmationTokenRepository.deleteById(id)
        return "token deleted"
    }

    override fun deleteConfirmationTokenByUser(user: UserEntity) {
        confirmationTokenRepository.deleteByUser(user)
    }

    override fun getRandomString() : String {
        val allowedChars = ('0'..'9')
        return (1..4)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private fun buildEmail(name: String, confirmationCode: String): String {
        return "<div style=\"font-family: Helvetica, Arial, sans-serif; font-size: 16px; margin: 0; color: #4CAF50;\">\n" +
                "    <span style=\"display: none; font-size: 1px; color: #fff; max-height: 0;\"></span>\n" +
                "\n" +
                "    <table role=\"presentation\" width=\"100%\" style=\"border-collapse: collapse; min-width: 100%; width: 100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "        <tbody>\n" +
                "            <tr>\n" +
                "                <td width=\"100%\" height=\"53\" bgcolor=\"#4CAF50\">\n" +
                "                    <table role=\"presentation\" width=\"100%\" style=\"border-collapse: collapse; max-width: 580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "                        <tr>\n" +
                "                            <td width=\"70\" bgcolor=\"#4CAF50\" valign=\"middle\">\n" +
                "                                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse: collapse\">\n" +
                "                                    <tr>\n" +
                "                                        <td style=\"padding-left: 10px\"></td>\n" +
                "                                        <td style=\"font-size: 28px; line-height: 1.315789474; Margin-top: 4px; padding-left: 10px\">\n" +
                "                                            <span style=\"font-family: Helvetica, Arial, sans-serif; font-weight: 700; color: #ffffff; text-decoration: none; vertical-align: top; display: inline-block\">\n" +
                "                                                Подтверждение почты SharingMap\n" +
                "                                            </span>\n" +
                "                                        </td>\n" +
                "                                    </tr>\n" +
                "                                </table>\n" +
                "                            </td>\n" +
                "                        </tr>\n" +
                "                    </table>\n" +
                "                </td>\n" +
                "            </tr>\n" +
                "        </tbody>\n" +
                "    </table>\n" +
                "\n" +
                "    <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse: collapse; max-width: 580px; width: 100%!important\" width=\"100%\">\n" +
                "        <tr>\n" +
                "            <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "            <td>\n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse: collapse\">\n" +
                "                    <tr>\n" +
                "                        <td bgcolor=\"#C8E6C9\" width=\"100%\" height=\"10\"></td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "            <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "\n" +
                "    <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse: collapse; max-width: 580px; width: 100%!important\" width=\"100%\">\n" +
                "        <tr>\n" +
                "            <td height=\"30\"><br></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "            <td style=\"font-family: Helvetica, Arial, sans-serif; font-size: 19px; line-height: 1.315789474; max-width: 560px\">\n" +
                "                <p style=\"Margin: 0 0 15px 0; font-size: 19px; line-height: 25px; color: #0b0c0c\">\n" +
                "                    Здравствуйте, " + name + "!\n" +
                "                </p>\n" +
                "                <p style=\"Margin: 0 0 15px 0; font-size: 19px; line-height: 25px; color: #0b0c0c\">\n" +
                "                    Спасибо за регистрацию в приложении SharingMap!\n" +
                "                </p>\n" +
                "                <p>\n" +
                "                    Чтобы подтвердить регистрацию, введите, пожалуйста, код в приложении:\n" +
                "                </p>\n" +
                "                <blockquote style=\"Margin: 0 0 25px 0; border-left: 10px solid #A5D6A7; padding: 15px 0 0.1px 15px; font-size: 19px; line-height: 25px\">\n" +
                "                    <p style=\"Margin: 0 0 15px 0; font-size: 19px; line-height: 25px; color: #0b0c0c\">\n" +
                "                        " + confirmationCode + "\n" +
                "                    </p>\n" +
                "                </blockquote>\n" +
                "                <br><p style=\"Margin: 0 0 10px 0; font-size: 16px; line-height: 1.315789474; color: #0b0c0c\">\n" +
                "                    С уважением,\n" +
                "                </p>\n" +
                "                <p style=\"Margin: 0 0 10px 0; font-size: 16px; line-height: 1.315789474; color: #0b0c0c\">\n" +
                "                    команда SharingMap\n" +
                "                </p>\n" +
                "                <p style=\"Margin: 0 0 10px 0; font-size: 16px; line-height: 1.315789474;\"> \n" +
                "    <a href=\"https://sharingmap.ru\" style=\"color: #4CAF50; text-decoration: none;\" target=\"_blank\">\n" +
                "        SharingMap.ru\n" +
                "    </a>\n" +
                "                </p>\n" +
                "                <p style=\"Margin: 0 0 10px 0; font-size: 16px; line-height: 1.315789474; color: #4CAF50;\">\n" +
                "    <a href=\"mailto:sharingmapru@gmail.com\" style=\"color: #4CAF50; text-decoration: none;\">\n" +
                "        sharingmapru@gmail.com\n" +
                "    </a>\n" +
                "</p>\n" +
                "            </td>\n" +
                "            <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td height=\"30\"><br></td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "\n" +
                "    <div class=\"yj6qo\"></div>\n" +
                "    <div class=\"adL\"></div>\n" +
                "</div>"
    }
}