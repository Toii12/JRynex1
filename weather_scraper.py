#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
天气数据采集脚本
抓取免费天气API数据，输出 weather_data.js 供 HTML 加载。

依赖：pip install requests
数据源：open-meteo.com（免费，无需 API Key）
"""

import json
import os
import sys

# ── 配置 ──────────────────────────────────────────
# 默认城市经纬度：深圳 (22.54, 114.06)
# 可修改为你所在城市的经纬度
LAT = 22.54
LON = 114.06
CITY = "深圳"

# 输出文件路径（与HTML同目录）
OUTPUT_FILE = os.path.join(os.path.dirname(os.path.abspath(__file__)), "weather_data.js")

# open-meteo API（免费，无需注册）
API_URL = (
    f"https://api.open-meteo.com/v1/forecast"
    f"?latitude={LAT}&longitude={LON}"
    f"&current=temperature_2m,weather_code,relative_humidity_2m,wind_speed_10m"
    f"&timezone=Asia/Shanghai"
)

# 天气代码 → 中文描述 + 图标类型
WEATHER_MAP = {
    0:  ("晴", "sun"),
    1:  ("少云", "cloud-sun"),
    2:  ("多云", "cloud"),
    3:  ("阴", "cloud"),
    45: ("雾", "fog"),
    48: ("雾凇", "fog"),
    51: ("小雨", "rain-light"),
    53: ("中雨", "rain"),
    55: ("大雨", "rain"),
    61: ("小雨", "rain-light"),
    63: ("中雨", "rain"),
    65: ("大雨", "rain-heavy"),
    66: ("冻雨", "rain"),
    67: ("冻雨", "rain-heavy"),
    71: ("小雪", "snow"),
    73: ("中雪", "snow"),
    75: ("大雪", "snow"),
    77: ("雪粒", "snow"),
    80: ("阵雨", "rain"),
    81: ("中阵雨", "rain"),
    82: ("大阵雨", "rain-heavy"),
    85: ("阵雪", "snow"),
    86: ("大阵雪", "snow"),
    95: ("雷暴", "thunder"),
    96: ("雷暴+冰雹", "thunder"),
    99: ("强雷暴", "thunder"),
}


def fetch_weather():
    """抓取天气数据"""
    try:
        import requests
    except ImportError:
        print("[ERROR] 缺少 requests 库，请运行: pip install requests")
        sys.exit(1)

    print(f"[INFO] 正在获取 {CITY} 天气... (lat={LAT}, lon={LON})")
    try:
        resp = requests.get(API_URL, timeout=15)
        resp.raise_for_status()
        data = resp.json()
    except requests.RequestException as e:
        print(f"[ERROR] 网络请求失败: {e}")
        sys.exit(1)

    current = data.get("current", {})
    if not current:
        print("[ERROR] API 返回数据异常")
        sys.exit(1)

    temp = round(current["temperature_2m"], 1)
    code = current["weather_code"]
    humidity = current.get("relative_humidity_2m", 0)
    wind = current.get("wind_speed_10m", 0)

    weather_info = WEATHER_MAP.get(code, ("未知", "cloud"))
    desc, icon = weather_info

    result = {
        "city": CITY,
        "temp": temp,
        "icon": icon,
        "desc": desc,
        "humidity": humidity,
        "wind": wind,
        "updated": resp.headers.get("date", "")
    }

    print(f"[OK] {CITY}: {temp}°C {desc} (湿度{humidity}% 风速{wind}km/h)")
    return result


def write_js(data):
    """将天气数据写入 weather_data.js"""
    js_content = "var WEATHER_DATA = " + json.dumps(data, ensure_ascii=False) + ";\n"
    with open(OUTPUT_FILE, "w", encoding="utf-8") as f:
        f.write(js_content)
    print(f"[OK] 已写入 {OUTPUT_FILE}")


def main():
    data = fetch_weather()
    write_js(data)


if __name__ == "__main__":
    main()
