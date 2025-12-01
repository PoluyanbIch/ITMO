package config

import (
	"log"

	"github.com/ilyakaznacheev/cleanenv"
)

type Config struct {
	Address       string `yaml:"address" env:"ADDRESS" env-default:"localhost:80"`
	DBAddress     string `yaml:"db_address" env:"DB_ADDRESS" env-default:"localhost:82"`
	LogLevel      string `yaml:"log_level" env:"LOG_LEVEL" env-default:"DEBUG"`
	JWTSecret     string `yaml:"jwt_secret" env:"JWT_SECRET" env-default:"aakhebfkajf"`
	RefreshSecret string `yaml:"refresh_secret" env:"REFRESH_SECRET" env-default:"slfjaglejbeg"`
	CorsOrigins   string `yaml:"cors_origins" env:"CORS_ORIGINS" env-default:"http://localhost:3000,http://frontend:80"`
}

func MustLoad(configPath string) Config {
	var cfg Config
	if err := cleanenv.ReadConfig(configPath, &cfg); err != nil {
		log.Fatalf("cannot read config %q: %s", configPath, err)
	}
	return cfg
}
