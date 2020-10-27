require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "react-native-usedesk"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  react-native-usedesk
                   DESC
  s.homepage     = "https://github.com/ARDcode/react-native-usedesk"
  s.license      = "MIT"
  # s.license    = { :type => "MIT", :file => "FILE_LICENSE" }
  s.authors      = { "ARDCode" => "codeard@gmail.com" }
  s.platform     = :ios, "10.0"
  s.source       = { :git => "https://github.com/ARDcode/react-native-usedesk.git" }

  s.swift_version = "5.0"
  s.source_files = "ios/**/*.{h,m,swift}"
  s.requires_arc = true

  s.dependency 'React'
  s.dependency 'UseDesk_SDK_Swift'

end