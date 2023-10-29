package com.sharingmap.security.registration

import java.util.*

class RegistrationResponse (val email: String,
                            val enabled: Boolean,
                            val confirmationTokenId: String
)