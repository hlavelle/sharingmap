package com.sharingmap.security.resetpassword

import com.sharingmap.entities.UserEntity
import com.sharingmap.security.email.EmailService
import com.sharingmap.security.email.EmailServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class PasswordTokenServiceImpl(
    private val emailService: EmailService,
    private val passwordTokenRepository: PasswordTokenRepository
): PasswordTokenService {

    val logger = LoggerFactory.getLogger(EmailServiceImpl::class.java)

    override fun createPasswordToken(user: UserEntity): PasswordTokenEntity {
        if (passwordTokenRepository.findByUser(user).isPresent) {
            deletePasswordTokenByUser(user)
            logger.info("old password token was deleted")
        }
        val token = getRandomString()

        val passwordToken =  PasswordTokenEntity(token, LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(15), user)

        savePasswordToken(passwordToken)

        buildEmail(user.username, token).let { emailService.send(user.email, it) }
        logger.info("mail with reset password code was sent")
        return passwordToken
    }

    override fun savePasswordToken(token: PasswordTokenEntity) {
        passwordTokenRepository.save(token)
        logger.info("token was created and saved")
    }

    @Transactional
    override fun confirmToken(token: String, tokenId: String): String {
        val confirmationToken = getToken(UUID.fromString(tokenId))

        return if (confirmationToken.isPresent && confirmationToken.get().token == token) {
            val user: UserEntity = confirmationToken.get().user
            val expiredAt: LocalDateTime? = confirmationToken.get().expiresAt
            if (expiredAt != null) {
                check(!expiredAt.isBefore(LocalDateTime.now())) { "token expired" }
            }
            user.enabled = true

            deleteToken(UUID.fromString(tokenId))

            "confirmed"
        } else {
            "can't confirm"
        }
    }

    override fun getToken(tokenId: UUID): Optional<PasswordTokenEntity> {
        return passwordTokenRepository.findById(tokenId)
    }

    override fun deleteToken(id: UUID): String {
        passwordTokenRepository.deleteById(id)
        logger.info("token was deleted")
        return "token deleted"
    }

    override fun deletePasswordTokenByUser(user: UserEntity) {
        passwordTokenRepository.deleteByUser(user)
    }

    override fun getRandomString() : String {
        val allowedChars = ('0'..'9')
        return (1..4)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private fun buildEmail(name: String, code: String): String {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Please click on the below link to change your password: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> " + code + " </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>"
    }

}