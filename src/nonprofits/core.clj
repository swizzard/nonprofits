(ns nonprofits.core
  (:require [nonprofits.parse :refer [match-names
                                      active-maps
                                      contracts-maps]]
            [cheshire.core :refer [generate-stream]]
            [clojure.java.io :as io]
            [clojure.string :refer [join]])
  (:gen-class))


(defn -main
  "I don't do a whole lot ... yet."
  []
  (with-open [wrtr (io/writer "results.json")]
    (generate-stream (join "\n" (pmap #(match-names % contracts-maps)
                                      active-maps))
                     wrtr)))
