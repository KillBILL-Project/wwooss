package com.bigbro.wwooss.v1.dto.request.notification;

import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.NotificationTemplateCode;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Getter
@Builder
public class NotificationSendRequest {
    private NotificationTemplateCode templateCode;

    private List<User> targets;

    private Map<String, String> variableMap;

    public boolean isEmptyTargets() {
        return CollectionUtils.isEmpty(this.targets);
    }

    public boolean isMultipleTarget() {
        if (this.isEmptyTargets()) {
            return false;
        }
        return this.targets.size() > 1;
    }
}
