package pl.umk.bugclassification.scmparser.git.parsers

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BlameParserSuite extends FunSuite {

  test("parsing correct blame") {
    val what = """64edf2ea8e6b82ed7945f1123beb02d60209a4d3 Main.hs     (Mikołaj Fejzer 2012-09-05 20:19:07 +0200  1) {-# LANGUAGE NoMonomorphismRestriction, OverloadedStrings,CPP, DeriveDataTypea
64edf2ea8e6b82ed7945f1123beb02d60209a4d3 Main.hs     (Mikołaj Fejzer 2012-09-05 20:19:07 +0200  2)     MultiParamTypeClasses, TemplateHaskell, TypeFamilies, RecordWildCards  #-}
4b6b5a03308e97f3f80d36db60fcdbba27ed6d61 Main.hs     (Mikołaj Fejzer 2012-09-09 18:03:54 +0200  3) module Main where 
c984bdd943ca2c9f3e90d1a69a975f485b34a843 Main.hs     (Mikołaj Fejzer 2012-09-05 21:13:36 +0200  4) import Control.Exception    ( bracket )
273b83f18c5426d32115161df4a4318ac654ef28 Main.hs     (Mikołaj Fejzer 2012-08-08 22:30:25 +0200  5) import Control.Monad (msum)
3836276186657c0fc469f41c1c0b46eb9fd61f4c Diagrams.hs (Mikołaj Fejzer 2012-08-01 20:52:11 +0200  6) import Control.Monad.Trans (lift, liftIO)
c984bdd943ca2c9f3e90d1a69a975f485b34a843 Main.hs     (Mikołaj Fejzer 2012-09-05 21:13:36 +0200  7) import Data.Acid            ( AcidState, Query, Update, openLocalState )
c984bdd943ca2c9f3e90d1a69a975f485b34a843 Main.hs     (Mikołaj Fejzer 2012-09-05 21:13:36 +0200  8) import Data.Acid.Advanced   ( query', update' )
c984bdd943ca2c9f3e90d1a69a975f485b34a843 Main.hs     (Mikołaj Fejzer 2012-09-05 21:13:36 +0200  9) import Data.Acid.Local      ( createCheckpointAndClose )
c984bdd943ca2c9f3e90d1a69a975f485b34a843 Main.hs     (Mikołaj Fejzer 2012-09-05 21:13:36 +0200 10) import Data.SafeCopy        ( base, deriveSafeCopy )
a142dba0c6878e03cd3c6eb2794ee6e41acd63fa Diagrams.hs (Mikołaj Fejzer 2012-07-29 20:01:52 +0200 11) import Data.Tree
65e52d2dfd0c062cda80d5f2296c265fa25c0483 Main.hs     (Mikołaj Fejzer 2012-08-02 21:17:00 +0200 12) import DiagramWrapper
273b83f18c5426d32115161df4a4318ac654ef28 Main.hs     (Mikołaj Fejzer 2012-08-08 22:30:25 +0200 13) import Graphics.Rendering.Diagrams.Core 
"""
    val result = BlameParser.parse(BlameParser.blameList, what)
    assert(result.successful)
    assert(result.get(0).sha1=="64edf2ea8e6b82ed7945f1123beb02d60209a4d3")
    assert(result.get(0).filename=="Main.hs")
    assert(result.get(5).sha1=="3836276186657c0fc469f41c1c0b46eb9fd61f4c")
    assert(result.get(5).filename=="Diagrams.hs")

  }
}