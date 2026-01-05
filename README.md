# 📚 Marginalia

> Share your reading journey with friends, one annotation at a time.

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen)
![Java](https://img.shields.io/badge/Java-21-orange)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![Status](https://img.shields.io/badge/Status-In%20Development-yellow)

## 🎯 The Vision

Ever wondered what your friends thought about that *exact* paragraph on page 47? **Marginalia** lets you share book annotations with friends while preventing spoilers—you'll only see notes up to your current reading progress.

### The Problem It Solves

- 📖 **Isolated reading**: You read alone, wondering what others think
- 💬 **Spoiler anxiety**: Book clubs and discussions risk ruining the story
- 📝 **Lost insights**: Your margin notes stay trapped in physical books

### The Solution

**Marginalia** creates a communal reading experience where:
- Friends' annotations appear as you progress through a book
- Spoiler prevention is automatic (based on your current page)
- Reading becomes a shared, social activity

---

## ✨ Features (Planned)

### Core Reading Experience
- 📚 **Track your books** - Add books you're currently reading
- 📝 **Leave annotations** - Mark meaningful passages with your thoughts
- 👥 **Follow friends** - See what resonates with your reading community
- 🔒 **Spoiler-safe** - Only see annotations up to your current page

### Social Features
- 💬 **Comment** on friends' annotations
- ❤️ **Like** insights that resonate
- 📊 **Reading feed** - Timeline of friends' recent annotations
- 🏆 **Most liked** - Discover the most impactful passages

---

## 🏗️ Architecture

### Tech Stack

**Backend**
- **Spring Boot 3.5.7** - RESTful API framework
- **Spring Data JPA** - Database ORM layer
- **PostgreSQL 16** - Relational database
- **Lombok** - Boilerplate reduction
- **Gradle 8.14** - Build tool

**Frontend** (Coming Soon)
- React or Next.js
- Tailwind CSS

### System Design
```
┌─────────────────┐
│   React App     │  ← User Interface
└────────┬────────┘
         │
    REST API
         │
┌────────▼────────┐
│  Spring Boot    │  ← Business Logic
│                 │    - Controllers
│                 │    - Services
│                 │    - Repositories
└────────┬────────┘
         │
    JDBC/JPA
         │
┌────────▼────────┐
│   PostgreSQL    │  ← Data Storage
└─────────────────┘
```

### Database Schema (Current)
```sql
users
├── id (PK, auto-increment)
├── username (unique)
├── email (unique)
└── joined_date

-- Coming soon:
-- books, annotations, friendships, likes, comments
```

---

## 🚀 Getting Started

### Prerequisites

- **Java 21** (JDK)
- **Docker** (for PostgreSQL)
- **Gradle 8.x** (included via wrapper)

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/mohamadacma/marginalia.git
cd marginalia
```

2. **Start PostgreSQL with Docker**
```bash
docker run --name pg-marginalia \
  -e POSTGRES_PASSWORD=secret \
  -e POSTGRES_DB=marginalia \
  -p 5432:5432 \
  -d postgres:16
```

3. **Configure database connection**

Edit `src/main/resources/application.yaml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/marginalia
    username: postgres
    password: secret
```

4. **Run the application**
```bash
./gradlew bootRun
```

The API will start at `http://localhost:8080`

---

## 📸 Development Journey

### Early Wireframes

<img src="https://bear-images.sfo2.cdn.digitaloceanspaces.com/moezying/flowarch.webp" alt="Flow Architecture" width="600">

*Initial architecture planning - mapping out how users, books, and annotations connect*

### Development Setup

<img src="https://bear-images.sfo2.cdn.digitaloceanspaces.com/moezying/springbottinit.webp" alt="Spring Boot Initialization" width="600">

*Spring Boot project initialization with all required dependencies*

---

## 🗺️ Roadmap

### ✅ Phase 1: Foundation (January 2026)
- [x] Project setup (Spring Boot + PostgreSQL)
- [x] User entity and database schema
- [ ] Book entity
- [ ] Annotation entity with page tracking
- [ ] UserBook relationship (track reading progress)

### 🚧 Phase 2: Core API (February 2026)
- [ ] REST endpoints for users
- [ ] REST endpoints for books
- [ ] REST endpoints for annotations
- [ ] Spoiler prevention logic
- [ ] Progress tracking

### 📅 Phase 3: Social Features (March 2026)
- [ ] Friendship system
- [ ] Annotation feed
- [ ] Likes and comments
- [ ] Most liked annotations

### 🎨 Phase 4: Frontend (April 2026)
- [ ] React UI
- [ ] Book search integration
- [ ] Annotation interface
- [ ] Reading progress tracker

---

## 🛠️ Current Development

### Daily Commit Challenge

Building this project one commit at a time throughout 2026. Follow the journey:

- **Day 1-3**: Project planning and setup
- **Day 4**: User entity created ✅
- **Day 5**: UserRepository (in progress)

---

## 🤝 Contributing

This is a learning project, but suggestions and feedback are welcome!

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 💭 Inspiration

> "Reading is a conversation with the author. Marginalia makes it a conversation with your friends too."

Built with ☕ and 📚 by Mohamad Ahmad

---

## 📧 Contact

- GitHub: [@mohamadacma](https://github.com/mohamadacma)
- Project Link: [https://github.com/mohamadacma/marginalia](https://github.com/mohamadacma/marginalia)

---

### 🌟 Star this repo if you're excited about social reading!
