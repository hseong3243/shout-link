package com.seong.shoutlink.domain.link.linkdomain.util;

import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DomainExtractor {

    private static final String HTTP = "http";
    private static final Pattern URL_PATTERN = Pattern.compile(
        "[-a-zA-Z0-9@:%_\\+.~#?&//=]{2,256}\\.[a-z]{2,4}\\b(\\/[-a-zA-Z0-9가-힣@:%_\\+.~#?&//=]*)?");
    private static final Set<String> SECOND_DOMAIN = new HashSet<>();

    static {
        SECOND_DOMAIN.addAll(
            List.of("co", "ne", "or", "re", "pe", "go", "mil", "ac", "hs", "ms", "es", "sc", "kg"));
    }

    public static String extractRootDomain(String url) {
        checkIsUrlPattern(url);
        String fullDomain = removePath(url);
        return removeSubDomain(fullDomain);
    }

    private static void checkIsUrlPattern(String url) {
        if(URL_PATTERN.matcher(url).matches()) {
            return;
        }
        throw new ShoutLinkException("입력값이 URL 형식이 아닙니다.", ErrorCode.ILLEGAL_ARGUMENT);
    }

    private static String removePath(String url) {
        if(url.startsWith(HTTP)) {
            URI uri = URI.create(url);
            return uri.getHost();
        }
        int firstSlashIndex = url.indexOf("/");
        if(firstSlashIndex == -1) {
            return url;
        }
        return url.substring(0, firstSlashIndex);
    }

    private static String removeSubDomain(String fullDomain) {
        String[] splitDomain = fullDomain.split("\\.");
        if(hasSecondDomain(splitDomain)) {
            return makeRootDomain(3, splitDomain);
        } else {
            return makeRootDomain(2, splitDomain);
        }
    }

    private static boolean hasSecondDomain(String[] splitDomain) {
        int totalDepth = splitDomain.length;
        if(totalDepth > 2) {
            String secondDomain = splitDomain[totalDepth - 2];
            return SECOND_DOMAIN.stream()
                .anyMatch(secondDomain::equals);
        }
        return false;
    }

    private static String makeRootDomain(int rootDomainDepth, String[] splitDomain) {
        int totalDepth = splitDomain.length;
        StringBuilder domainBuilder = new StringBuilder();
        for (int depth = rootDomainDepth; depth > 0; depth--) {
            domainBuilder.append(splitDomain[totalDepth - depth]);
            if(depth > 1) {
                domainBuilder.append(".");
            }
        }
        return domainBuilder.toString();
    }
}
