CatParadise Hotel – webová aplikace pro rezervace ubytování koček
CatParadise Hotel je webová aplikace určená majitelům koček, kteří si chtějí na čas své zvíře nechat ubytovat v hotelu. Uživatelé mohou vyhledat dostupné termíny, vybrat vhodný typ pokoje a vytvořit rezervaci pobytu pro svou kočku samotnou nebo v cestě. Registrace uživatele není povinná – lze vytvořit rezervaci i anonymně. Pokud máte účet, rezervace se přiřazují k vašemu profilu, kde lze spravovat kočky, rezervace, osobní údaje a nastavení notifikací.

Technologický stack
Frontend: čistý HTML/CSS/JavaScript, spouštěný přes Node.js server na portu `3000`.

Backend: Java aplikace postavená na Spring Boot (REST API), běžící na portu `8080`.

Databáze: MySQL 8.0 na portu `3306` s navrženým schématem pro uživatele, rezervace, pokoje a kočky.

UI nástroj pro databázi: MySQL Workbench pro návrh modelu a spuštění SQL skriptů.

Dokumentace API: Swagger UI přístupný na `http://localhost:8080/swagger-ui.html`.

Aplikace je určena k lokálnímu spuštění na `localhost` z repozitáře na GitHubu.

📋 Jak si projekt nastavit a spustit
CatParadise Hotel je webová aplikace s frontendem na Node.js (port `3000`) a backendem v Javě (Spring Boot, port `8080`), který komunikuje s databází MySQL (port `3306`).

1. Stažení projektu
```bash
git clone https://github.com/EliNattsu/Rocnikovy-projekt-II.git
cd catparadise-hotel
```
2. MySQL server a Workbench
Nainstalujte MySQL Server (např. verze 8.0) a ujistěte se, že běží na `localhost:3306`.

Nainstalujte MySQL Workbench a připojte se k lokálnímu serveru.

Vytvořte schéma (např. `catparadise`) a proveďte SQL skript z projektu, který vytvoří potřebné tabulky (`users`, `reservations`, `rooms`, `cats` apod.).

3. Spuštění frontendu (Node.js)
Přejděte do frontendu a spusťte:

```bash
cd frontend
npm install
npm run dev
```
Frontend běží na `http://localhost:3000` a komunikuje s backendem na `http://localhost:8080`.

4. Nastavení databáze v IntelliJ IDEA
Otevřete backend projekt (`catparadise-hotel/backend`) v IntelliJ IDEA.

V `src/main/resources/application.properties` nastavte připojení:

```text
spring.datasource.url=jdbc:mysql://localhost:3306/catparadise
spring.datasource.username=root
spring.datasource.password=vase-heslo-root
spring.jpa.hibernate.ddl-auto=update
Ujistěte se, že název schémat Ultimate a uživatelské údaje odpovídají vaší konfiguraci MySQL.
```

5. Spuštění backendu (Spring Boot)
Můžete backend spustit dvěma způsoby:

Přes IntelliJ IDEA:

Otevřete třídu `CatparadisehotelApplication` a spusťte ji přes Run.

Přímo přes Maven:

```bash
cd backend
./mvnw spring-boot:run
```
Backend běží na `http://localhost:8080`.

6. Swagger – prohlížení a testování API
Po spuštění backendu otevřete v prohlížeči:

`
http://localhost:8080/swagger-ui.html
`
Na této stránce můžete přehledně vidět všechny dostupné endpointy a testovat je přímo v prohlížeči.
