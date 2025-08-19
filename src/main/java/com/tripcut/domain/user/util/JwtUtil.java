//private String buildToken(String email, String role, String typ, long ttlMs, Key key) {
//    Date now = new Date();
//    Date exp = new Date(now.getTime() + ttlMs);
//
//    return Jwts.builder()
//            .setSubject(email)
//            .claim("role", role)   // 최소 클레임만 보관
//            .claim("typ", typ)     // access/refresh 구분
//            .setIssuedAt(now)
//            .setExpiration(exp)
//            .signWith(key, SignatureAlgorithm.HS256)
//            .compact();
//}