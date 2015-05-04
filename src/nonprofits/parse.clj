(ns nonprofits.parse
  (:require [clojure.string :as string]
            [clojure.java.io :as io]
            [clojure.data.csv :refer [read-csv]]))

(defmacro seq-read [fname & funcs]
  `(with-open [rdr (io/reader ~fname)]
     (doall (-> rdr line-seq ~@funcs))))
    
;; filename constants
(def resdir "resources/")
(def contracts-fname (str resdir "NYC nonprofits with contracts.csv"))
(def active-fname (str resdir "Active_Corporations___Beginning_1800.csv"))

(def contracts-keys [:ein :name :address :code])
;(def active-maps (let [k-line (seq-read active-fname first read-csv)
;                       v-lines (seq-read active-fname next (partial map read-csv))]))
                   
;(def active-keys (map keyword 
;                      (seq-read active-fname
;                                first
;                                read-csv)))
;(def active-lines (map read-csv
;                       (seq-read active-fname
;                                 next)))

;(def contracts-maps (map (partial zipmap contracts-keys)
;                         (seq-read 
;                         (with-open [rdr (io/reader contracts-fname)]))))
