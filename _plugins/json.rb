require 'json'

module JsonFilter
  def json2(input)
    input.to_json
  end

  Liquid::Template.register_filter self
end