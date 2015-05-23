(ns nonprofits.parse
  (:require [clojure.string :as string]
            [clojure.java.io :as io]
            [clojure.data.csv :refer [read-csv]]
            [iota]))

(defmacro seq-read [fname & funcs]
  `(with-open [rdr (io/reader ~fname)]
     (doall (-> rdr line-seq ~@funcs))))

;; filename constants
(def resdir "resources/")
(def contracts-fname (str resdir "NYC nonprofits with contracts.csv"))
(def active-fname (str resdir "Active_Corporations___Beginning_1800.csv"))

(def contracts-keys [:ein :name :address :code])

(def active
  (let [f-seq (iota/seq active-fname)
        ks (first (read-csv (first f-seq)))]
    (map (partial zipmap ks)
         (mapcat read-csv (rest f-seq)))))

(def contracts
  (let [f-seq (iota/seq contracts-fname 256 \return)]
    (map (partial zipmap contracts-keys)
         (mapcat read-csv (rest f-seq)))))
