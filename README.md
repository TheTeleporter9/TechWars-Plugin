# TechWars Minecraft Plugin

A Minecraft plugin that manages team assignments from Supabase database.

## Features

- Automatically assigns players to teams based on Supabase database
- Colored team prefixes in chat and tab list
- Team colors: Red, Blue, Green, Yellow, Purple
- Refresh teams manually with `/refreshteams` command
- Teams update on server startup and player join

## Setup

1. Build the plugin:
   ```bash
   mvn clean package
   ```

2. Copy the generated JAR from `target/` to your server's `plugins/` directory

3. Start your server once to generate the config file

4. Edit `plugins/TechWars/config.yml` and add your Supabase credentials:
   ```yaml
   supabase:
     url: "https://your-project.supabase.co/rest/v1/minecraft_teams"
     api-key: "your-api-key-here"
   ```

5. Restart the server

## Configuration

Edit `plugins/TechWars/config.yml`:

```yaml
supabase:
  url: "https://your-project.supabase.co/rest/v1/minecraft_teams"
  api-key: "your-api-key-here"
```

## Commands

- `/refreshteams` - Refresh all teams from database (requires `techwars.refresh` permission)
- `/refreshteams player` - Refresh your own team assignment

## Permissions

- `techwars.refresh` - Allows use of `/refreshteams` command

## Database Schema

Your Supabase table should have the following columns:
- `discord_id` (text/number)
- `discord_name` (text)
- `minecraft_username` (text)
- `team` (text) - Team name (e.g., "Green Team", "Red Team", etc.)

## Supported Teams

- Red
- Blue
- Green
- Yellow
- Purple

Team names can include " Team" suffix (e.g., "Green Team" will be normalized to "Green")

## Requirements

- Paper/Spigot 1.20.1
- Java 8+

## Building

```bash
mvn clean package
```

The compiled JAR will be in `target/Techwars-1.0-SNAPSHOT.jar`

