(ns rps.migration
  (:require [clojure.java.jdbc :as sql]))

(def spec (System/getenv "DATABASE_URL"))

(defn migrated? []
  (-> (sql/query spec
                 [(str "select count(*) from information_schema.tables "
                       "where table_name='dataset'")])
      first :count pos?))

(defn migrate []
  (when (not (migrated?))
    (print "Creating database structure...") (flush)
    (sql/db-do-commands spec
                        (sql/create-table-ddl
                         :dataset
                         [[:id :serial "PRIMARY KEY"]
                         [:element :varchar "NOT NULL"]
                         ]))
    (println " done")))
