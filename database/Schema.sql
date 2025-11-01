BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "Appartiene" (
	"id_titolo"	INTEGER NOT NULL,
	"id_genere"	INTEGER NOT NULL,
	PRIMARY KEY("id_titolo","id_genere"),
	FOREIGN KEY("id_genere") REFERENCES "Genere"("id_genere"),
	FOREIGN KEY("id_titolo") REFERENCES "Titolo"("id_titolo") ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "Genere" (
	"id_genere"	INTEGER NOT NULL,
	"nome_genere"	INTEGER NOT NULL,
	PRIMARY KEY("id_genere")
);
CREATE TABLE IF NOT EXISTS "Piattaforma" (
	"id_piattaforma"	INTEGER NOT NULL,
	"nome_piattaforma"	TEXT,
	PRIMARY KEY("id_piattaforma")
);
CREATE TABLE IF NOT EXISTS "PreferisceGenere" (
	"id_utente"	INTEGER NOT NULL,
	"id_genere"	INTEGER NOT NULL,
	PRIMARY KEY("id_utente","id_genere"),
	FOREIGN KEY("id_genere") REFERENCES "Genere"("id_genere") ON DELETE CASCADE,
	FOREIGN KEY("id_utente") REFERENCES "Utente"("id_utente") ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "PreferiscePiattaforma" (
	"id_utente"	INTEGER NOT NULL,
	"id_piattaforma"	INTEGER NOT NULL,
	PRIMARY KEY("id_utente","id_piattaforma"),
	FOREIGN KEY("id_piattaforma") REFERENCES "Piattaforma"("id_piattaforma") ON DELETE CASCADE,
	FOREIGN KEY("id_utente") REFERENCES "Utente"("id_utente") ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "ProgressoSerie" (
	"id_utente"	INTEGER NOT NULL,
	"id_serie"	INTEGER NOT NULL,
	"id_stagione"	INTEGER,
	"id_episodio"	INTEGER,
	PRIMARY KEY("id_utente","id_serie"),
	FOREIGN KEY("id_serie") REFERENCES "Titolo"("id_titolo"),
	FOREIGN KEY("id_utente") REFERENCES "Utente"("id_utente") ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "SelezioneTitolo" (
	"id_utente"	INTEGER NOT NULL,
	"id_titolo"	INTEGER NOT NULL,
	"tipo_lista"	TEXT NOT NULL CHECK(tipo_lista IN ('Whatclist','Visionati')),
	"e_preferito"	BLOB NOT NULL DEFAULT 'false',
	PRIMARY KEY("id_utente","id_titolo"),
	FOREIGN KEY("id_titolo") REFERENCES "Titolo"("id_titolo"),
	FOREIGN KEY("id_utente") REFERENCES "Utente"("id_utente") ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "Titolo" (
	"id_titolo"	INTEGER NOT NULL,
	"nome_titolo"	TEXT NOT NULL,
	"tipologia"	TEXT NOT NULL CHECK("tipologia" IN ('SerieTv', 'Film')),
	"trama"	TEXT,
	"immagine"	BLOB,
	"voto_medio"	INTEGER,
	"durata_minuti"	INTEGER,
	"anno_pubblicazione"	INTEGER,
	PRIMARY KEY("id_titolo")
);
CREATE TABLE IF NOT EXISTS "TrasmessoSu" (
	"id_titolo"	INTEGER NOT NULL,
	"id_piattaforma"	INTEGER NOT NULL,
	PRIMARY KEY("id_titolo","id_piattaforma"),
	FOREIGN KEY("id_piattaforma") REFERENCES "Piattaforma"("id_piattaforma"),
	FOREIGN KEY("id_titolo") REFERENCES "Titolo"("id_titolo")
);
CREATE TABLE IF NOT EXISTS "Utente" (
	"id_utente"	INTEGER NOT NULL,
	"nome"	TEXT NOT NULL,
	"email"	TEXT NOT NULL,
	"password"	TEXT NOT NULL,
	"domanda_sicurezza"	TEXT NOT NULL,
	"risposta_domanda_sicurezza"	TEXT NOT NULL,
	PRIMARY KEY("id_utente" AUTOINCREMENT)
);
INSERT INTO "Titolo" ("id_titolo","nome_titolo","tipologia","trama","immagine","voto_medio","durata_minuti","anno_pubblicazione") VALUES (1,'A','Film',NULL,NULL,NULL,NULL,NULL),
 (2,'B','SerieTv',NULL,NULL,NULL,NULL,NULL);
CREATE TRIGGER controllo_se_serieTV
BEFORE INSERT ON ProgressoSerie
FOR EACH ROW
BEGIN
	SELECT
		CASE
		  WHEN NOT EXISTS(
		  SELECT 1 FROM Titolo where id_titolo=new.id_serie AND
			tipologia='SerieTv')
			THEN
			 RAISE(ABORT, 'Errore:Elemento inserito non è una serieTv')
END;
END;
COMMIT;
