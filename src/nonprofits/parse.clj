(ns nonprofits.parse
  (:require [clojure.string :as string]
            [clojure.data.csv :refer [read-csv]]
            [iota :as io]
            [nonprofits.levenshtein :refer [levenshtein]]))

  
;; filename constants
(def resdir "resources/")
(def contracts-fname (str resdir "NYC nonprofits with contracts.csv"))
(def active-fname (str resdir "Active_Corporations___Beginning_1800.csv"))

(def contracts-keys [:ein :name :address :code])

(def active-maps 
    (let [lines (map read-csv (io/seq active-fname))
          ks (map (fn [x] (-> x (string/replace " " "-") keyword)) (ffirst lines))]
      (map #(as-> % $ (apply (partial zipmap ks) $)
                      (select-keys $ [:Initial-DOS-Filing-Date
                                      :DOS-Process-Name
                                      :Current-Entity-Name])
                      (assoc $ :name (distinct [(:DOS-Process-Name $)
                                                (:Current-Entity-Name $)]))
                      (dissoc $ :DOS-Process-Name :Current-Entity-Name))
           (rest lines))))

(def contracts-maps 
    (map #(try (as-> % $ (read-csv $)
                         (apply (partial zipmap contracts-keys) $))
            (catch Exception _ {}))
         ;; contracts csv lines end in "\r"
         (io/seq contracts-fname 256 \return)))

(defn flatten-names [m]
  (try 
    (map #(assoc m :name %) (:name m))
  (catch Exception _ nil)))
  
(defn match-names [contracts-map active-maps]
  (let [flattened-maps (mapcat flatten-names active-maps)
        name-to-match (:name contracts-map)
        match-maps (fn [m2]
                      (let [n2 (:name m2)]
                        (if (= name-to-match n2)
                          (assoc m2 :score 0 :match-for name-to-match)
                          (assoc m2
                           :score (levenshtein (string/lower-case name-to-match) 
                                               (string/lower-case n2))
                           :match-for name-to-match))))]
    (loop [t (try 
               (next flattened-maps)
              (catch Exception _ nil))
           best-match (match-maps (first flattened-maps))]
      (if (nil? t) (merge best-match contracts-map)
          (recur (next t)
               (let [new-match (match-maps (first t))]
                 (if (< (:score new-match) (:score best-match))
                   new-match
                   best-match)))))))
