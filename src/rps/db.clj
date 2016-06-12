(ns rps.db
  (:require [clojure.java.jdbc :as sql]))

(def db-spec {:classname "org.h2.Driver"
              :subprotocol "h2:file"
              :subname "db/rps"})

(defn add-element-to-db
  [el]
  (let [results (sql/with-connection db-spec
                  (sql/insert-record :dataset
                                     {:element el}))]
    (assert (= (count results) 1))
    (first (vals results))))

(defn set-partition
  []
  (let [results (sql/with-connection db-spec
                  (sql/with-query-results res
                    ["select element from dataset"]
                    (doall res)))]
     (partition-all 3 (shuffle results))))

(defn get-all-data
  []
  (let [results (sql/with-connection db-spec
                  (sql/with-query-results res
                    ["select id, element from dataset"]
                    (doall res)))]
    results))
