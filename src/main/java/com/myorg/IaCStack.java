package com.myorg;

import software.constructs.Construct;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.IVpc;
import software.amazon.awscdk.services.ec2.Instance;
import software.amazon.awscdk.services.ec2.InstanceClass;
import software.amazon.awscdk.services.ec2.InstanceSize;
import software.amazon.awscdk.services.ec2.InstanceType;
import software.amazon.awscdk.services.ec2.MachineImage;
import software.amazon.awscdk.services.ec2.Peer;
import software.amazon.awscdk.services.ec2.Port;
import software.amazon.awscdk.services.ec2.SecurityGroup;
import software.amazon.awscdk.services.ec2.SubnetSelection;
import software.amazon.awscdk.services.ec2.SubnetType;
import software.amazon.awscdk.services.ec2.UserData;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.ec2.VpcLookupOptions;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.iam.Role;

public class IaCStack extends Stack {
    public IaCStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public IaCStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        Integer NumberInstance = 3;

        IVpc vpc = Vpc.fromLookup(this, "vpc-0ed34b15fb09db0ec",
                VpcLookupOptions.builder().isDefault(Boolean.TRUE).build());

        SecurityGroup iaCSecurityGroup = SecurityGroup.Builder.create(this, "IaCSecurityGroup")
                .vpc(vpc)
                .securityGroupName("IaCSecurityGroup")
                .allowAllOutbound(true)
                .build();

        iaCSecurityGroup.addIngressRule(Peer.anyIpv4(), Port.tcp(22), "Allows ssh");
        iaCSecurityGroup.addIngressRule(Peer.anyIpv4(), Port.tcp(80), "Allows http");
        iaCSecurityGroup.addIngressRule(Peer.anyIpv4(), Port.tcp(8080), "Allows http");
        iaCSecurityGroup.addIngressRule(Peer.anyIpv4(), Port.tcp(443), "Allows https");
        iaCSecurityGroup.addIngressRule(Peer.anyIpv4(), Port.tcp(42000), "Allows docker port");

        IRole labRole = Role.fromRoleArn(this, "LabRole ", "arn:aws:iam::752601222007:role/LabRole");

        // Comandos
        UserData userData = UserData.forLinux();
        userData.addCommands("sudo yum update -y",
                "sudo yum install docker",
                "sudo service docker start",
                "sudo docker run -d -p 42000:6000 --name firstdockerimageaws mjtoni/firstsparkwebapprepo");

        // Instancias EC2
        for (int i = 0; i < NumberInstance; i++) {
            Instance.Builder
                    .create(this, "iac-ec2" + i)
                    .instanceType(InstanceType.of(InstanceClass.T3, InstanceSize.MICRO))
                    .vpc(vpc)
                    .securityGroup(iaCSecurityGroup)
                    .vpcSubnets(
                            SubnetSelection.builder()
                                    .subnetType(SubnetType.PUBLIC)
                                    .build())
                    .role(labRole)
                    .machineImage(MachineImage.latestAmazonLinux2())
                    .keyName(id)
                    .userData(userData)
                    .build();
        }

    }
}
