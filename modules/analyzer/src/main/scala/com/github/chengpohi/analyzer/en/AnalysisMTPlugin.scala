package com.github.chengpohi.analyzer.en

import org.elasticsearch.index.analysis.{CharFilterFactory, TokenFilterFactory, TokenizerFactory}
import org.elasticsearch.indices.analysis.AnalysisModule.AnalysisProvider
import org.elasticsearch.plugins.{AnalysisPlugin, Plugin}

/**
  * Created by chengpohi on 13/02/2017.
  */
class AnalysisMTPlugin extends Plugin with AnalysisPlugin {
  override def getCharFilters(): java.util.Map[java.lang.String, AnalysisProvider[CharFilterFactory]] = {
    null
  }

  override def getTokenFilters(): java.util.Map[java.lang.String, AnalysisProvider[TokenFilterFactory]] = {
    null
  }

  override def getTokenizers(): java.util.Map[java.lang.String, AnalysisProvider[TokenizerFactory]] = {
    null
  }
}
