package com.example.blog.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.util.Base64;

// 쿠키 관련 로직을 모아놓은 클래스
public class CookieUtil {

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        // ex) CookieUtil.addCookie(response, "name", "value", 3600);
        Cookie cookie = new Cookie(name, value); // static 을 사용해서 인스턴스 없이 직접 호출
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }
        // 가져온 쿠키들에서 이름이 같은 쿠키가 있다면 삭제하는 코드
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                // 삭제 후 쿠키에 추가.
                response.addCookie(cookie);
            }
        }
    }

    // 객체를 직렬화해 쿠키의 값으로 변환 => Base64를 사용하면 이진 데이터를 문자열로 변환할 수 있다.
    public static String serialize(Object obj){
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(obj));
    }

    // 쿠키를 역직렬화해 객체로 변환
    public static <T> T deserialize(Cookie cookie, Class<T> cls){
        return cls.cast(
                SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(cookie.getValue())));
    }
}
