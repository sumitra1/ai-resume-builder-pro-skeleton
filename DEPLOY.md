# Deploy Resume AI Analyzer (free tier)

Stack:

| Component | Free service |
|-----------|----------------|
| Frontend (React) | [Vercel](https://vercel.com) |
| Backend (Spring Boot) | [Render](https://render.com) |
| PostgreSQL | [Neon](https://neon.tech) |
| ChromaDB | Render (second free web service) |

Free-tier notes:

- Render web services **sleep after ~15 min** of inactivity (cold start ~30–60s).
- Neon free tier is generous for hobby use.
- Uploaded PDFs on Render use **ephemeral disk** (files may be lost on redeploy).

---

## 1. PostgreSQL on Neon

1. Sign up at [neon.tech](https://neon.tech).
2. Create a project (e.g. `resume-ai-analyzer`).
3. Open **Connection details** → copy:
   - Host, database, user, password
4. Build the JDBC URL:

```text
jdbc:postgresql://<HOST>/<DATABASE>?sslmode=require
```

Example:

```text
jdbc:postgresql://ep-cool-name-123456.us-east-2.aws.neon.tech/neondb?sslmode=require
```

Save for Render env vars:

- `DATABASE_URL` = JDBC URL above
- `DATABASE_USERNAME` = neon user
- `DATABASE_PASSWORD` = neon password

---

## 2. Backend + Chroma on Render

### Option A — Blueprint (recommended)

1. Push this repo to GitHub.
2. Go to [Render Dashboard](https://dashboard.render.com) → **New** → **Blueprint**.
3. Connect the repo; Render reads `render.yaml`.
4. Set **manual** env vars when prompted:
   - `GEMINI_API_KEY` — your Google Gemini API key
   - `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD` — from Neon
   - `FRONTEND_URL` — set after Vercel deploy (e.g. `https://your-app.vercel.app`)
5. Deploy. You get:
   - `resume-ai-api` → `https://resume-ai-api.onrender.com`
   - `resume-ai-chroma` → `https://resume-ai-chroma.onrender.com`

`CHROMA_ENDPOINT` is wired from the Chroma service URL automatically.

### Option B — Manual

Create two **Web Services** (Docker, free plan):

**Chroma**

- Dockerfile path: `docker/chroma.Dockerfile`
- Docker context: repo root
- Health check: `/api/v2/heartbeat`

**API**

- Dockerfile path: `backend/Dockerfile`
- Docker context: `backend`
- Health check: `/api/health`
- Env:
  - `SPRING_PROFILES_ACTIVE=prod`
  - `GEMINI_API_KEY`
  - `JWT_SECRET` (random long string)
  - `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`
  - `CHROMA_ENDPOINT=https://<your-chroma-service>.onrender.com`
  - `FRONTEND_URL=https://<your-vercel-app>.vercel.app`

Verify API:

```text
https://resume-ai-api.onrender.com/api/health
→ {"status":"up"}
```

---

## 3. Frontend on Vercel

1. Sign up at [vercel.com](https://vercel.com).
2. **Add New Project** → import GitHub repo.
3. Settings:
   - **Root Directory**: `frontend`
   - **Build Command**: `npm run build`
   - **Output Directory**: `dist`
4. Environment variable:

| Name | Value |
|------|--------|
| `VITE_API_URL` | `https://resume-ai-api.onrender.com/api` |

5. Deploy and copy your URL (e.g. `https://resume-ai-analyzer.vercel.app`).

6. Update Render **API** service env:

```text
FRONTEND_URL=https://your-app.vercel.app
```

Redeploy the API so CORS allows your frontend.

---

## 4. Local development env

**Backend** (`backend/.env` — do not commit):

```bash
GEMINI_API_KEY=your-gemini-key
JWT_SECRET=local-dev-secret-at-least-32-chars
```

Run:

```bash
cd backend
export GEMINI_API_KEY=...
mvn spring-boot:run
```

**Frontend** (`frontend/.env`):

```bash
VITE_API_URL=http://localhost:8080/api
```

```bash
cd frontend
npm run dev
```

**Chroma** (Docker):

```bash
docker compose up -d chromadb
```

---

## 5. After deploy checklist

- [ ] `GET /api/health` returns `up`
- [ ] Register / login on Vercel URL
- [ ] Upload resume (re-upload after fresh Chroma)
- [ ] Chat, Analysis, Job Match work
- [ ] `FRONTEND_URL` matches Vercel URL exactly (no trailing slash)

---

## Troubleshooting

| Issue | Fix |
|-------|-----|
| **Registration / login fails** with generic error | 1) On Render API: set `FRONTEND_URL` to your exact Vercel URL (no trailing slash) and **Manual Deploy**. 2) On Vercel: set `VITE_API_URL=https://resume-ai-api-d63g.onrender.com/api` and **Redeploy** (env vars are baked in at build time). |
| CORS error in browser console | Set `FRONTEND_URL` on Render to exact Vercel URL; redeploy API. Backend also allows `https://*.vercel.app` after latest deploy. |
| Chroma 404 collection | Collection auto-creates on upload; re-upload resume |
| 502 / slow first request | Render free tier cold start — wait and retry |
| Gemini errors | Check `GEMINI_API_KEY` on Render |
| DB connection failed | Verify Neon JDBC URL and `sslmode=require` |
