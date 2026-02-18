package com.matrimony.auth.controller;

public class TodoClass {
    // todo: if mobile and email is changed otp verification status must be changed and user staus has to be changed.
    // todo : to know BCryptPasswordEncoder
    // todo: FE dont store the token in local strogae and Store in HttpOnly cookie Js cannot read it Prevents XSS token theft
    // During logout what should i do the token, how to refers the token
    // todo: Silent Token Refresh
    // ✅ Access Token → 10–15 mins
    //✅ Refresh Token → 7 days
    //✅ Rotate Refresh Token (very important)
    // Access Token = Stateless → Scalable
    //Refresh Token = Stateful → Controlled

    //In frontend:
    //
    //Before token expires (like at 14th minute):
    //
    //Client automatically calls refresh API in background.
    //
    //User will not see 401.
    //
    //This is called:
    //
    //👉 Silent Token Refresh
    // Generate → ACTIVE
    //Wrong attempt → ACTIVE
    //Max attempts → BLOCKED
    //Time expired → EXPIRED
    //Success → USED

}
