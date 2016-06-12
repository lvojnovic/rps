(ns rps.views
  (:require [rps.db :as db]
            [clojure.string :as str]
            [hiccup.page :as hic-p]))

(defn gen-page-head
  [title]
  [:head
   [:title (str "RPS: " title)]
   (hic-p/include-css "/css/styles.css")])

(def header-links
  [:div#header-links
   "[ "
   [:a {:href "/"} "Pregled upisanih natjecatelja"]
   " | "
   [:a {:href "/add-element"} "Dodaj novog"]
   " | "
   [:a {:href "/partition"} "Odredi grupe"]
   " ]"])

(defn home-page
  []
  (let [all-data (db/get-all-data)]
    (hic-p/html5
     (gen-page-head "Pregled upisanih natjecatelja")
     header-links
     [:h1 "Pregled upisanih natjecatelja"]
     [:table
      [:tr [:th "Br."] [:th "Natjecatelj"] ]
      (for [el all-data]
        [:tr [:td (:id el)] [:td (:element el)] ])])))

(defn add-element-page
  []
  (hic-p/html5
   (gen-page-head "Dodaj natjecatelja")
   header-links
   [:h1 "Dodaj natjecatelja"]
   [:form {:action "/add-element" :method "POST"}
    [:p "Ime: " [:input {:type "text" :name "element"}]]
    [:p [:input {:type "submit" :value "Dodaj"}]]]))

(defn add-element-results-page
  [{:keys [element]}]
  (let [id (db/add-element-to-db element)]
    (hic-p/html5
     (gen-page-head "Natjecatelj dodan")
     header-links
     [:h1 "Natjecatelj dodan"]
     [:p "Dodan [" element "] (br: " id ") na popis natjecatelja. "])))

(defn partition
  []
  (let [set-partitions (db/set-partition)]
    (hic-p/html5
     (gen-page-head "Grupe")
     header-links
     [:h1 "Grupe"]
     [:ol
      (for [group set-partitions]
        [:li
         [:ul
          (for [el group]
            [:li (:element el) ])]])])))
