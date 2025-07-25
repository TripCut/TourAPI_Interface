package com.tripcut.core.components;

import java.util.Optional;

public interface AuditAware<T> {
    Optional<T> getCurrentAuditor();
}
