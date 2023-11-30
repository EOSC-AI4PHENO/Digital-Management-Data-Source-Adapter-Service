alter table "source"."T_SOURCE_CONFIGURATION"
        add column if not exists obj text;

alter table "source"."T_SOURCE_CONFIGURATION"
         add column if not exists "enable" Boolean;