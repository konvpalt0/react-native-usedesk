
Pod::Spec.new do |s|
  s.name         = "RNUsedesk"
  s.version      = "1.0.0"
  s.summary      = "RNUsedesk"
  s.description  = <<-DESC
                  RNUsedesk
                   DESC
  s.homepage     = ""
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "author@domain.cn" }
  s.platform     = :ios, "7.0"
  s.source       = { :git => "https://github.com/author/RNUsedesk.git", :tag => "master" }
  s.requires_arc = true
  s.swift_version = "5.0"
  s.source_files = "ios/**/*.{h,m}"
  s.requires_arc = true

  s.dependency 'React-Core'
  s.dependency 'UseDesk_SDK_Swift', :git => 'https://github.com/ARDcode/UseDeskSwift.git', :modular_headers => true'


end

  