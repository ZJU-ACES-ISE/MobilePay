-- 站点间费用表
CREATE TABLE IF NOT EXISTS site_fare (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    from_site_id BIGINT NOT NULL COMMENT '起始站点ID',
    to_site_id BIGINT NOT NULL COMMENT '终点站点ID',
    city_code VARCHAR(10) NOT NULL COMMENT '城市编码',
    transit_type VARCHAR(10) NOT NULL COMMENT '交通类型（SUBWAY地铁，BUS公交）',
    base_fare DECIMAL(10,2) NOT NULL COMMENT '基础票价',
    distance DOUBLE COMMENT '距离（公里）',
    station_count INT COMMENT '站点数量',
    status VARCHAR(10) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态（ACTIVE正常，INACTIVE停用）',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_from_site_id (from_site_id),
    INDEX idx_to_site_id (to_site_id),
    INDEX idx_city_code (city_code),
    UNIQUE KEY uk_from_to_site (from_site_id, to_site_id, transit_type),
    FOREIGN KEY (from_site_id) REFERENCES site(id) ON DELETE CASCADE,
    FOREIGN KEY (to_site_id) REFERENCES site(id) ON DELETE CASCADE
);

-- 通行码表
CREATE TABLE IF NOT EXISTS transit_pass (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    city_id VARCHAR(10) NOT NULL COMMENT '城市ID',
    city_name VARCHAR(50) NOT NULL COMMENT '城市名称',
    code_url VARCHAR(255) NOT NULL COMMENT '通行码链接',
    status VARCHAR(10) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态（ACTIVE正常，INACTIVE停用）',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_city_id (city_id),
    UNIQUE KEY uk_user_city (user_id, city_id),
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

-- 默认通行码数据（北京、上海、广州、深圳）
INSERT INTO transit_pass (id, user_id, city_id, city_name, code_url, status, created_time, updated_time)
VALUES
    (1, 1, '1100', '北京', 'https://example.com/transit/pass/beijing.png', 'ACTIVE', NOW(), NOW()),
    (2, 1, '3100', '上海', 'https://example.com/transit/pass/shanghai.png', 'ACTIVE', NOW(), NOW()),
    (3, 1, '4401', '广州', 'https://example.com/transit/pass/guangzhou.png', 'ACTIVE', NOW(), NOW()),
    (4, 1, '4403', '深圳', 'https://example.com/transit/pass/shenzhen.png', 'ACTIVE', NOW(), NOW())
ON DUPLICATE KEY UPDATE
    city_name = VALUES(city_name),
    code_url = VALUES(code_url),
    status = VALUES(status),
    updated_time = NOW(); 