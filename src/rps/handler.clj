(ns rps.handler
  (:require [rps.views :as views]
            [compojure.core :as cc]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [rps.migration :as schema]) 
  (:gen-class))


(cc/defroutes app-routes
  (cc/GET "/"
       []
       (views/home-page))
  (cc/GET "/add-element"
       []
       (views/add-element-page))
  (cc/POST "/add-element"
        {params :params}
        (views/add-element-results-page params))
  (cc/GET "/partition"
       []
       (views/partition))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))

(defn -main
  [& [port]]
  (schema/migrate)
  (let [port (Integer. (or port
                           (System/getenv "PORT")
                           5000))]
    (jetty/run-jetty #'app {:port  port
                            :join? false})))
