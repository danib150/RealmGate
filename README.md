# 🌐 RealmGate

**RealmGate** is a lightweight **backend routing and server management plugin** for **Hytale**.  
It provides a **configuration-driven, distributed system** for routing players between backend servers **without relying on external proxy software**.

RealmGate uses:

- **Redis** → real-time routing & player presence  
- **MySQL / MariaDB** → persistent backend server storage  

---

## ✨ Features

✔ Config-based backend server registration  
✔ Default server routing on player join  
✔ Cross-server player movement  
✔ Real-time server health checks (heartbeat)  
✔ Player presence tracking (who is online & where)  
✔ No external proxy required  

---

## 🧠 Architecture Overview

### 🗄 MySQL / MariaDB
Used to:
- Store the **persistent list of backend servers**

### ⚡ Redis
Used for:
- Server heartbeats  
- Online server detection  
- Player presence tracking  
- Cross-server move requests  

### 🎮 Hytale `referToServer` API
- Moves players between backend servers

Each backend server runs **RealmGate independently**  
All communication happens via **Redis**

---

## 📋 Commands

### `/server list`
Lists all registered backend servers and their status.

```
lobby   (127.0.0.1:5520) ONLINE
vanilla (127.0.0.1:5530) OFFLINE
```

### `/server move <server>`
Moves yourself to another server.

```
/server move vanilla
```

### `/server move <player> <server>`
Moves a specific player to another server.

```
/server move Notch vanilla
```

### `/whoami`
Shows which server you are currently connected to.

```
You are connected to: lobby
```

### `/where <player>`
Shows where a player is currently located (if online).

```
Notch is currently on: vanilla
```

---

## 🧰 Requirements

### ☕ Java
- **Java 25 or newer**

### ⚡ Redis (Required)
Used for:
- Server health checks (heartbeat)
- Player presence
- Cross-server movement

Minimal configuration:

```
redis.host: 127.0.0.1
redis.port: 6379
```

Local or remote • Docker supported

### 🗄 MySQL / MariaDB (Required)
Used for:
- Persistent backend server registry

Minimal configuration:

```
mysql.host: 127.0.0.1
mysql.port: 3306
mysql.database: realmgate
mysql.username: root
mysql.password: secret
```

---

## 🔧 How to Compile

RealmGate depends on the **official Hytale server JAR**, which is **proprietary software** and **not included** in this repository.

### Build Requirements
- Java 25+
- Gradle
- Official Hytale Server JAR

### Setup

1. Download the official Hytale downloader  
   https://downloader.hytale.com/hytale-downloader.zip

2. Use it to obtain the official Hytale server JAR

3. Place the JAR here:
```
libs/HytaleServer.jar
```

4. Build the plugin:
```
./gradlew build
```

---

## ⚙️ Configuration Example

```
server-name: lobby
server-public-address: 127.0.0.1
server-port: 5520

redis.host: 127.0.0.1
redis.port: 6379
redis.database: 0
redis.timeout-ms: 2000

mysql.host: 127.0.0.1
mysql.port: 3306
mysql.database: realmgate
mysql.username: root
mysql.password: secret
```

---

## 🚀 Notes

- Each backend server must have a **unique server-name**
- Redis must be shared across all backend servers
- MySQL / MariaDB must be shared across all backend servers
- RealmGate is **not a proxy**
- Routing is **fully distributed**
- Player movement is handled via **Redis-based move requests**

---

## 📄 License

MIT
