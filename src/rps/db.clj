(ns rps.db
  (:require [clojure.java.jdbc :as sql]))

(def db-spec (System/getenv "DATABASE_URL"))

(defn add-element-to-db
  [el]
  (let [results (sql/insert! db-spec :dataset {:element el} )]
    (assert (= (count results) 1))
    (first (vals results))))

(defn set-partition
  []
  (let [results (into [] (sql/query db-spec ["select element from dataset"]))]
     (partition-all 2 (shuffle results))))

(defn get-all-data
  []
  (into [] (sql/query db-spec ["select * from dataset"])))
