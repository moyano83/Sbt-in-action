object Logic{
	def matchLikelihood(kitten:Kitten, buyerPreferences:BuyerPreferences):Double ={
    buyerPreferences.attributes.map(pref => kitten.attributes.contains(pref) match{
      case true => 1.0
      case false => 0.0
    }
    ).reduce((a,b) => a+b) / buyerPreferences.attributes.size
	}
}
